package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.folder.CreateFolderDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.folder.RenameFolderDTO;
import com.example.springbootfilestorage.dto.mappers.FolderDTOMapper;
import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final UserContext userContext;
    private final FolderDTOMapper folderDTOMapper;
    private final int MAX_PARENT_FOLDERS = 5;
    private final DeleteFileService deleteFileService;

    public FolderService(FolderRepository folderRepository, FileRepository fileRepository, UserContext userContext,
                         FolderDTOMapper folderDTOMapper, DeleteFileService deleteFileService) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.userContext = userContext;
        this.folderDTOMapper = folderDTOMapper;
        this.deleteFileService = deleteFileService;
    }

    public FolderDTO saveFolder(CreateFolderDTO createFolderDTO, UUID parentUuid) {
        Folder folder = new Folder();
        folder.setName(createFolderDTO.folderName());
        folder.setOwner(userContext.getAuthenticatedUser());
        folder.setDeleted(false);
        if (parentUuid != null) {
            Folder parent = folderRepository.findByUuid(parentUuid).orElse(null);
            if (parent == null) throw new IllegalArgumentException("Parent folder not found");
            folder.setParent(parent);
            if (parent.getSubfolders().size() >= MAX_PARENT_FOLDERS)
                throw new IllegalArgumentException("Maximum number of subfolders reached");
        } else folder.setParent(null);

        folderRepository.save(folder);
        return folderDTOMapper.apply(folder);
    }

    public FolderDTO renameFolder(UUID uuid, RenameFolderDTO renameFolderDTO) {
        Folder folder = folderRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("Folder not found"));

        folder.setName(renameFolderDTO.newFolderName());
        folderRepository.save(folder);

        return folderDTOMapper.apply(folder);
    }

    public List<FolderDTO> getAllFolders() {
        return folderRepository.findAllFolders(userContext.getAuthenticatedUser())
                .stream()
                .map(folderDTOMapper)
                .toList();
    }


    public void deleteFolder(UUID uuid) {
        Folder folder = setFolder(uuid);
        if (folder == null) throw new RuntimeException("Folder to be deleted not found");
        setDeletedRecursively(folder, true);
        folderRepository.save(folder);
    }

    public List<FolderDTO> searchFoldersByName(String searchTerm) {
        return folderRepository.searchFoldersByName(searchTerm, userContext.getAuthenticatedUser())
                .stream()
                .map(folderDTOMapper)
                .toList();
    }

    public FolderDTO findHomeDTO() {
        User currentUser = userContext.getAuthenticatedUser();
        List<Folder> folders = folderRepository.findAllFoldersWithNoParents(currentUser);
        List<UploadedFile> subfolderIds = fileRepository.findAllFilesWithNoFolder(currentUser);
        Folder homeFolder = new Folder();
        homeFolder.setOwner(currentUser);
        homeFolder.setParent(null);
        homeFolder.setSubfolders(folders);
        homeFolder.setFiles(subfolderIds);
        return folderDTOMapper.apply(homeFolder);
    }


    public Folder findByUuid(UUID uuid) {
        return folderRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("Folder not found"));
    }

    public FolderDTO findByDTOUuid(UUID uuid) {
        return folderDTOMapper.apply(folderRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Folder not found")));
    }

    public List<FolderSummaryDTO> findAllDeletedFolders() {
        return folderRepository.findAllDeletedFolders(userContext.getAuthenticatedUser())
                .stream()
                .map(f -> new FolderSummaryDTO(f.getUuid(), f.getName()))
                .toList();
    }

    public void restoreFolder(UUID uuid) {
        Folder folder = setFolder(uuid);
        if (folder == null) throw new RuntimeException("Folder to be restored not found");
        setDeletedRecursively(folder, false);
        folderRepository.save(folder);
    }

    @Transactional
    public void deleteFolderPermanently(UUID uuid) {
        Folder folder = setFolder(uuid);
        if (folder == null) return;
        deleteFolderPermanently(folder);
    }

    @Transactional
    public void deleteFoldersToBeDeleted(LocalDate threshold) {
        folderRepository.findAllByDeletedTrueAndDeletedAtBefore(threshold).stream()
                .filter(folder -> folder.getParent() == null || !folder.getParent().isDeleted())
                .forEach(this::deleteFolderPermanently);
    }

    private void setDeletedRecursively(Folder folder, boolean deleted) {
        folder.setDeleted(deleted);
        folder.setDeletedAt(deleted ? LocalDate.now() : null);
        folder.getFiles().forEach(f -> {
            f.setDeleted(deleted);
            f.setDeletedAt(deleted ? LocalDate.now() : null);
        });
        folder.getSubfolders().forEach(s -> setDeletedRecursively(s, deleted));
    }

    private void deleteFolderPermanently(Folder folder) {
        folder.getSubfolders().forEach(this::deleteFolderPermanently);
        folder.getFiles().forEach(deleteFileService::deleteFileOnMachine);
        Folder parent = folder.getParent();
        if (parent != null) {
            parent.getSubfolders().remove(folder);
            folderRepository.save(parent);
        } else {
            folderRepository.delete(folder);
        }
    }

    private Folder setFolder(UUID uuid) {
        return folderRepository.findByUuid(uuid).orElse(null);
    }
}
