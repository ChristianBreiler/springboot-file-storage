package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.FolderDTO;
import com.example.springbootfilestorage.dto.UploadedFileDTO;
import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    public FolderService(FolderRepository folderRepository, FileRepository fileRepository) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
    }

    public Folder saveFolder(String name, Long parentId) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setOwner(null);
        if (parentId != null) {
            Folder parent = folderRepository.findById(parentId).orElse(null);
            if (parent == null) throw new IllegalArgumentException("Parent folder not found");
            folder.setParent(parent);
        }
        folderRepository.save(folder);
        return folder;
    }

    public Folder renameFolder(Long id, String newName) {
        Folder folder = folderRepository.findById(id).orElse(null);
        if (folder == null) return null;

        folder.setName(newName);
        folderRepository.save(folder);

        return folder;
    }

    public void deleteFolder(Long id) {
        Folder folder = folderRepository.findById(id).orElse(null);
        if (folder == null) return;
        folderRepository.delete(folder);
    }

    public Folder findById(Long id) {
        return folderRepository.findById(id).orElse(null);
    }

    public List<Folder> findAllFoldersWithNoParents() {
        //  Long userId = userContextService.getCurrentUserId();
        return folderRepository.findAllFoldersWithNoParents();
    }

    public List<Folder> searchFoldersByName(Long parentFolderId, String name) {
        return parentFolderId != null ?
                folderRepository.searchFoldersByName(parentFolderId, name) :
                folderRepository.findFoldersByNameOnHomePage(name);
    }

    public void moveFolderToFolder(Long folderId, long folderTargetId) {
        Folder folder = folderRepository.findById(folderId).orElse(null);
        if (folder == null) throw new IllegalArgumentException("Folder not found");
        Folder targetFolder = folderRepository.findById(folderTargetId).orElse(null);
        if (targetFolder == null) throw new IllegalArgumentException("Target folder not found");
        folder.setParent(targetFolder);
        folderRepository.save(folder);
    }

    private FolderDTO createDTO(Folder folder) {
        if (folder == null) return null;
        return new FolderDTO(
                folder.getId(),
                folder.getName(),
                folder.getParent() != null ? folder.getParent().getId() : null,
                // TODO: Change this later to the user id
                null,
                folder.getSubfolders().stream().map(this::createFolderSummaryDTO).toList(),
                folder.getFiles().stream().map(this::createUploadedFileDTO).toList()
        );
    }

    public FolderDTO findByDTOId(Long id) {
        return createDTO(folderRepository.findById(id).orElse(null));
    }

    public FolderDTO findHomeDTO() {
        List<Folder> folders = folderRepository.findAllFoldersWithNoParents();
        List<UploadedFile> subfolderIds = fileRepository.findAllFilesWithNoFolder();
        Folder homeFolder = new Folder();
        homeFolder.setOwner(null);
        homeFolder.setParent(null);
        homeFolder.setSubfolders(folders);
        homeFolder.setFiles(subfolderIds);
        return createDTO(homeFolder);
    }

    private FolderSummaryDTO createFolderSummaryDTO(Folder folder) {
        return new FolderSummaryDTO(folder.getId(), folder.getName());
    }

    // TODO: Duplicate from Fileservice
    private UploadedFileDTO createUploadedFileDTO(UploadedFile file) {
        if (file == null) return null;

        // TODO: Fix later
        Long folderId = file.getFolder() != null ? file.getFolder().getId() : null;

        return new UploadedFileDTO(
                file.getId(),
                folderId,
                file.getOriginalFilename(),
                file.getSize(),
                file.getFiletype());
    }
}
