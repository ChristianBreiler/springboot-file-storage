package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Filetype;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.file.CreateFileDTO;
import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.mappers.UploadFileDTOMapper;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.scripts.system.StoragePathBean;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FolderService folderService;
    private final FolderRepository folderRepository;
    private final Path UPLOAD_DIR;
    private final Path PROFILE_PIC_DIR;
    private final UserRepository userRepository;
    private final SettingsService settingsService;
    private final UserContext userContext;
    private final UploadFileDTOMapper uploadFileDTOMapper;

    public FileService(FileRepository fileRepository, FolderService folderService, FolderRepository folderRepository,
                       StoragePathBean storagePathBean, UserRepository userRepository, SettingsService settingsService,
                       UserContext userContext, UploadFileDTOMapper uploadFileDTOMapper) {
        this.fileRepository = fileRepository;
        this.folderService = folderService;
        this.folderRepository = folderRepository;
        UPLOAD_DIR = storagePathBean.getStorageFolderPath();
        PROFILE_PIC_DIR = storagePathBean.getProfilePicFolderPath();
        this.userRepository = userRepository;
        this.settingsService = settingsService;
        this.userContext = userContext;
        this.uploadFileDTOMapper = uploadFileDTOMapper;
    }

    public UploadedFile getFileByFileShareCode(String fileShareCode) {
        return fileRepository.finaByFileShareCode(fileShareCode);
    }

    public UploadedFileDTO saveFile(CreateFileDTO createFileDTO, UUID folderUuid) {
        MultipartFile file = createFileDTO.file();
        if (fileExists(file.getOriginalFilename(), folderUuid)) throw new IllegalArgumentException("File already exists");

        UploadedFile uploadedFile = new UploadedFile();

        String originalName = file.getOriginalFilename();
        String storedName = generateUniqueFileName(originalName);

        uploadedFile.setOriginalFilename(originalName);
        uploadedFile.setStoredName(storedName);
        uploadedFile.setSize(file.getSize());

        uploadedFile.setOwner(userContext.getAuthenticatedUser());
        uploadedFile.setProfilePic(false);

        Path filePath = UPLOAD_DIR.resolve(storedName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO: Notify the user about the error (e.g., via log or message)
            e.printStackTrace();
            return null;
        }

        uploadedFile.setStoragePath(filePath.toString());
        Filetype filetype = getFileType(file);
        if (filetype == null) return null;
        uploadedFile.setFiletype(filetype);

        if (folderUuid != null) {
            Folder folder = folderService.findByUuid(folderUuid);
            if (folder == null) return null;
            uploadedFile.setFolder(folder);
        }

        // uploadedFile.setCreatedAt(LocalDate.now());
        uploadedFile.setFileShareCode(UUID.randomUUID().toString());
        uploadedFile.setDeleted(false);
        fileRepository.save(uploadedFile);
        return uploadFileDTOMapper.apply(uploadedFile);
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) extension = originalFilename.substring(dotIndex);

        return UUID.randomUUID().toString() + extension;
    }

    private Filetype getFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) return null;
        String[] split = fileName.split("\\.");
        if (split.length < 2) return null;
        String extension = split[split.length - 1];
        return switch (extension.toLowerCase()) {
            case "pdf" -> Filetype.PDF;
            case "png" -> Filetype.PNG;
            case "jpg" -> Filetype.PNG;
            case "jpeg" -> Filetype.PNG;
            case "mp3" -> Filetype.MP3;
            default -> null;
        };
    }

    public List<UploadedFileDTO> findAllDeletedFiles() {
        return fileRepository.findAllDeletedFiles(userContext.getAuthenticatedUser())
                .stream()
                .map(uploadFileDTOMapper)
                .toList();
    }

    public boolean deleteFile(UUID uuid) {
        UploadedFile file = fileRepository.findByUuid(uuid).orElse(null);
        if (file == null) return false;

        file.setDeleted(true);
        // TODO: Handle deleting folders with deleted files
        file.setFinalDeletionDate(LocalDate.now().plusWeeks(3));
        try {
            fileRepository.save(file);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public UploadedFileDTO restoreFile(UUID uuid) {
        UploadedFile file = fileRepository.findByUuid(uuid).orElse(null);
        if (file == null) return null;

        file.setDeleted(false);
        file.setFinalDeletionDate(null);
        // TODO: Handle deleting folders with deleted files
        if (file.getFolder() != null && folderRepository.existsByUuid(file.getFolder().getUuid()))
            file.setFolder(null);
        fileRepository.save(file);
        return uploadFileDTOMapper.apply(file);
    }

    public void deleteFilePermanently(UUID uuid) {
        UploadedFile file = fileRepository.findByUuid(uuid).orElse(null);
        if (file == null) throw new IllegalArgumentException("File not found: " + uuid);
        if (!deleteFileOnMachine(file.getStoragePath()))
            throw new RuntimeException("Failed to delete file from machine");
        fileRepository.delete(file);
    }

    // Delete the physical file on the machine in the given directory
    private boolean deleteFileOnMachine(String filepath) {
        try {
            Path path = Paths.get(filepath);
            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UploadedFile> searchFilesByName(UUID uuid, String name) {
        // Special case since on the homepage file.folder == null and doesn't have an id
        if (uuid == null)
            return fileRepository.findFilesByNameOnHomePage(name);
        return fileRepository.findByNameAndId(uuid, name);
    }

    // File with same name in folder already exists
    public boolean fileExists(String name, UUID folderUuid) {
        return fileRepository.findByNameAndFolderUuid(name, folderUuid) != null;
    }

    public void saveProfilePic(MultipartFile file) {
        if (file.isEmpty()) return;

        UploadedFile uploadedFile = new UploadedFile();

        String originalName = file.getOriginalFilename();
        String storedName = generateUniqueFileName(originalName);

        uploadedFile.setOriginalFilename(originalName);
        uploadedFile.setStoredName(storedName);
        uploadedFile.setSize(file.getSize());

        User user = userContext.getAuthenticatedUser();
        uploadedFile.setOwner(user);
        user.setProfilePic(uploadedFile);
        UploadedFile oldProfilePic = user.getProfilePic();
        uploadedFile.setProfilePic(true);

        deleteFilePermanently(oldProfilePic.getUuid());

        Path filePath = PROFILE_PIC_DIR.resolve(storedName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO: Notify the user about the error (e.g., via log or message)
            e.printStackTrace();
            return;
        }

        uploadedFile.setStoragePath(filePath.toString());
        Filetype filetype = getFileType(file);
        if (filetype == null) return;
        uploadedFile.setFiletype(filetype);

        // uploadedFile.setCreatedAt(LocalDate.now());
        fileRepository.save(uploadedFile);
        // To update the user in the database with the new profile pic
        // userRepository.save(user);
    }

    // Used for drag and drop
    public void moveFileToFolder(Long fileId, Long folderId) {
        UploadedFile file = fileRepository.findById(fileId).orElse(null);
        if (file == null) throw new IllegalArgumentException("File not found");
        Folder folder = folderService.findById(folderId);
        file.setFolder(folder);
        fileRepository.save(file);
    }

    public UploadedFile renameFile(UUID uuid, String newName) {
        UploadedFile file = fileRepository.findByUuid(uuid).orElse(null);
        if (file == null) return null;
        file.setOriginalFilename(newName);
        fileRepository.save(file);
        return file;
    }

    public List<UploadedFileDTO> findAll() {
        return fileRepository
                .findAllFiles(userContext.getAuthenticatedUser())
                .stream()
                .map(uploadFileDTOMapper)
                .toList();
    }

    public UploadedFileDTO findDTOByUuid(UUID uuid) {
        return uploadFileDTOMapper.apply(findByUuid(uuid));
    }

    public UploadedFile findByUuid(UUID uuid) {
        return fileRepository.findByUuid(uuid).orElse(null);
    }
}
