package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.folder.CreateFolderDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.mappers.FolderDTOMapper;
import com.example.springbootfilestorage.dto.mappers.UploadFileDTOMapper;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final UserContext userContext;
    private final UploadFileDTOMapper uploadFileDTOMapper;
    private final FolderDTOMapper folderDTOMapper;
    private final int MAX_PARENT_FOLDERS = 5;

    public FolderService(FolderRepository folderRepository, FileRepository fileRepository, UserContext userContext,
                         UploadFileDTOMapper uploadFileDTOMapper, FolderDTOMapper folderDTOMapper) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.userContext = userContext;
        this.uploadFileDTOMapper = uploadFileDTOMapper;
        this.folderDTOMapper = folderDTOMapper;
    }

    public FolderDTO saveFolder(CreateFolderDTO createFolderDTO, Long parentId) {
        Folder folder = new Folder();
        folder.setName(createFolderDTO.folderName());
        folder.setOwner(userContext.getAuthenticatedUser());
        folder.setCreatedAt(LocalDate.now());
        folder.setUpdatedAt(LocalDate.now());
        if (parentId != null) {
            Folder parent = folderRepository.findById(parentId).orElse(null);
            if (parent == null) throw new IllegalArgumentException("Parent folder not found");
            folder.setParent(parent);
            if (parent.getSubfolders().size() >= MAX_PARENT_FOLDERS)
                throw new IllegalArgumentException("Maximum number of subfolders reached");
        } else {
            folder.setParent(null);
        }
        folderRepository.save(folder);
        return folderDTOMapper.apply(folder);
    }

    public Folder renameFolder(Long id, String newName) {
        Folder folder = folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));

        folder.setName(newName);
        folderRepository.save(folder);

        return folder;
    }

    public void deleteFolder(Long id) {
        Folder folder = folderRepository.findById(id).orElse(null);
        if (folder == null) throw new RuntimeException("Folder to be deleted not found");
        folderRepository.delete(folder);
    }

    public Folder findById(Long id) {
        return folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));
    }

    public List<Folder> searchFoldersByName(Long parentFolderId, String name) {
        return parentFolderId != null ?
                folderRepository.searchFoldersByName(parentFolderId, name) :
                folderRepository.findFoldersByNameOnHomePage(name);
    }

    public FolderDTO findHomeDTO() {
        List<Folder> folders = folderRepository.findAllFoldersWithNoParents(userContext.getAuthenticatedUser());
        List<UploadedFile> subfolderIds = fileRepository.findAllFilesWithNoFolder();
        Folder homeFolder = new Folder();
        homeFolder.setOwner(userContext.getAuthenticatedUser());
        homeFolder.setParent(null);
        homeFolder.setSubfolders(folders);
        homeFolder.setFiles(subfolderIds);
        return folderDTOMapper.apply(homeFolder);
    }

    public void moveFolderToFolder(Long folderId, long folderTargetId) {
        Folder folder = folderRepository.findById(folderId).orElse(null);
        if (folder == null) throw new IllegalArgumentException("Folder not found");
        Folder targetFolder = folderRepository.findById(folderTargetId).orElse(null);
        if (targetFolder == null) throw new IllegalArgumentException("Target folder not found");
        folder.setParent(targetFolder);
        folderRepository.save(folder);
    }

    public FolderDTO findByDTOId(Long id) {
        return folderDTOMapper.apply(folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found")));
    }
}
