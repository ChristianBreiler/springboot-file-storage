package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.folder.ParentFolderDTO;
import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final UserContext userContext;

    public FolderService(FolderRepository folderRepository, FileRepository fileRepository, UserContext userContext) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.userContext = userContext;
    }

    public FolderDTO saveFolder(String name, Long parentId) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setOwner(userContext.getAuthenticatedUser());
        folder.setCreatedAt(LocalDate.now());
        folder.setUpdatedAt(LocalDate.now());
        if (parentId != null) {
            Folder parent = folderRepository.findById(parentId).orElse(null);
            if (parent == null) throw new IllegalArgumentException("Parent folder not found");
            folder.setParent(parent);
        } else {
            // Homepage folders
            folder.setParent(null);
        }
        folderRepository.save(folder);
        return createDTO(folder);
    }

    public Folder renameFolder(Long id, String newName) {
        Folder folder = folderRepository.findById(id).orElse(null);
        if (folder == null) throw new RuntimeException("Folder to be renamed not found");

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
        return createDTO(homeFolder);
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
        return createDTO(folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found")));
    }

    private FolderDTO createDTO(Folder folder) {
        if (folder == null) return null;
        return new FolderDTO(
                folder.getId(),
                folder.getName(),
                userContext.getAuthenticatedUser().getId(),
                // Do this so the breadcrumb order is correct and not reversed
                allParents(folder).stream().map(this::createParentFolderDTO).collect(Collector.of(LinkedList::new,
                        LinkedList::addFirst, (a, b) -> {
                            b.addAll(a);
                            return b;
                        })),
                folder.getSubfolders() != null ?
                        folder.getSubfolders().stream().map(this::createFolderSummaryDTO).toList() : List.of(),
                folder.getFiles() != null ?
                        folder.getFiles().stream().map(this::createUploadedFileDTO).toList() : List.of()
        );
    }

    private List<Folder> allParents(Folder folder) {
        List<Folder> parents = new ArrayList<>();
        Folder currentFolder = folder;
        // Add max depth to prevent infinite loop
        int MAX_DEPTH = 10;
        int depth = 0;
        while (currentFolder.getParent() != null && depth++ < MAX_DEPTH) {
            parents.add(currentFolder.getParent());
            currentFolder = currentFolder.getParent();
        }
        return parents;
    }

    private ParentFolderDTO createParentFolderDTO(Folder folder) {
        if (folder == null) return null;
        return new ParentFolderDTO(folder.getId(), folder.getName());
    }

    private FolderSummaryDTO createFolderSummaryDTO(Folder folder) {
        return new FolderSummaryDTO(folder.getId(), folder.getName());
    }

    // TODO: Duplicate from Fileservice
    private UploadedFileDTO createUploadedFileDTO(UploadedFile file) {
        if (file == null) return null;
        return new UploadedFileDTO(
                file.getId(),
                file.getFolder() != null ? file.getFolder().getId() : null,
                file.getOriginalFilename(),
                file.getSize(),
                file.getFiletype());
    }
}
