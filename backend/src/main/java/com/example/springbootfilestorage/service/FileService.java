package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Filetype;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.MoveFileDto;
import com.example.springbootfilestorage.dto.file.CreateFileDTO;
import com.example.springbootfilestorage.dto.file.RenameFileDTO;
import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.mappers.FolderDTOMapper;
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
    private final UserContext userContext;
    private final UploadFileDTOMapper uploadFileDTOMapper;
    private final FolderDTOMapper folderDTOMapper;

    public FileService(FileRepository fileRepository, FolderService folderService, FolderRepository folderRepository,
                       StoragePathBean storagePathBean, UserContext userContext, UploadFileDTOMapper uploadFileDTOMapper,
                       FolderDTOMapper folderDTOMapper) {
        this.fileRepository = fileRepository;
        this.folderService = folderService;
        this.folderRepository = folderRepository;
        UPLOAD_DIR = storagePathBean.getStorageFolderPath();
        this.userContext = userContext;
        this.uploadFileDTOMapper = uploadFileDTOMapper;
        this.folderDTOMapper = folderDTOMapper;
    }

    public UploadedFileDTO saveFile(CreateFileDTO createFileDTO, UUID folderUuid) {
        MultipartFile file = createFileDTO.file();
        if (fileExists(file.getOriginalFilename(), folderUuid))
            throw new IllegalArgumentException("File already exists");

        UploadedFile uploadedFile = new UploadedFile();

        String originalName = file.getOriginalFilename();
        if (originalName == null) throw new IllegalArgumentException("File name is null");
        String storedName = generateUniqueFileName(originalName);

        uploadedFile.setOriginalFilename(originalName);
        uploadedFile.setStoredName(storedName);
        uploadedFile.setSize(file.getSize());

        uploadedFile.setOwner(userContext.getAuthenticatedUser());

        Path filePath = UPLOAD_DIR.resolve(storedName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
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

        uploadedFile.setDeleted(false);
        fileRepository.save(uploadedFile);
        return uploadFileDTOMapper.apply(uploadedFile);
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
        UploadedFile file = fileRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("File not found"));

        file.setDeleted(false);
        file.setFinalDeletionDate(null);
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

    public List<UploadedFileDTO> searchFilesByName(String searchTerm) {
        return fileRepository.findByNameAndId(searchTerm)
                .stream()
                .map(uploadFileDTOMapper)
                .toList();
    }

    // File with the same name in the folder already exists
    public boolean fileExists(String name, UUID folderUuid) {
        return fileRepository.findByNameAndFolderUuid(name, folderUuid) != null;
    }

    public void saveProfilePic(MultipartFile file) {
        if (file.isEmpty()) return;

        UploadedFile uploadedFile = new UploadedFile();

        String originalName = file.getOriginalFilename();
        if (originalName == null) throw new IllegalArgumentException("File name is null");
        String storedName = generateUniqueFileName(originalName);

        uploadedFile.setOriginalFilename(originalName);
        uploadedFile.setStoredName(storedName);
        uploadedFile.setSize(file.getSize());

        User user = userContext.getAuthenticatedUser();
        uploadedFile.setOwner(user);

        Filetype filetype = getFileType(file);
        if (filetype == null) return;
        uploadedFile.setFiletype(filetype);

        fileRepository.save(uploadedFile);
    }

    public FolderDTO moveFileToFolder(MoveFileDto moveFileDto) {
        UploadedFile file = fileRepository.findByUuid(moveFileDto.fileUuid()).orElseThrow(()
                -> new RuntimeException("File not found"));
        if (file == null) throw new IllegalArgumentException("File not found");
        Folder folder = moveFileDto.folderUuid() != null ? folderService.findByUuid(moveFileDto.folderUuid()) : null;
        Folder originalFolder = file.getFolder();
        file.setFolder(folder);
        fileRepository.save(file);
        // Return the original folder to update the UI
        if (originalFolder == null) {
            // In case the file was originally in the home folder, update the home folder
            User currentUser = userContext.getAuthenticatedUser();
            List<Folder> folders = folderRepository.findAllFoldersWithNoParents(currentUser);
            List<UploadedFile> subfolderIds = fileRepository.findAllFilesWithNoFolder(currentUser);
            Folder homeFolder = new Folder();
            homeFolder.setOwner(currentUser);
            homeFolder.setParent(null);
            homeFolder.setSubfolders(folders);
            homeFolder.setFiles(subfolderIds);
            return folderDTOMapper.apply(homeFolder);
        } else
            return folderDTOMapper.apply(originalFolder);
    }

    public UploadedFileDTO renameFile(UUID uuid, RenameFileDTO renameFileDTO) {
        UploadedFile file = fileRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("File not found:"));
        file.setOriginalFilename(renameFileDTO.newFileName() + '.' + renameFileDTO.fileType().toLowerCase());
        fileRepository.save(file);
        return uploadFileDTOMapper.apply(file);
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
            return false;
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        // Rename the file when storing it on the server to avoid name conflicts
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
}
