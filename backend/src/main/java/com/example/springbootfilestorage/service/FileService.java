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
import com.example.springbootfilestorage.dto.mappers.MetaDataDTOPMapper;
import com.example.springbootfilestorage.dto.mappers.UploadFileDTOMapper;
import com.example.springbootfilestorage.dto.metadata.MetaDataDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.scripts.system.StoragePathBean;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private final MetaDataDTOPMapper metaDataDTOPMapper;
    private final DeleteFileService deleteFileService;

    public FileService(FileRepository fileRepository, FolderService folderService, FolderRepository folderRepository,
                       StoragePathBean storagePathBean, UserContext userContext, UploadFileDTOMapper uploadFileDTOMapper,
                       FolderDTOMapper folderDTOMapper, MetaDataDTOPMapper metaDataDTOPMapper, DeleteFileService deleteFileService) {
        this.fileRepository = fileRepository;
        this.folderService = folderService;
        this.folderRepository = folderRepository;
        UPLOAD_DIR = storagePathBean.getStorageFolderPath();
        this.userContext = userContext;
        this.uploadFileDTOMapper = uploadFileDTOMapper;
        this.folderDTOMapper = folderDTOMapper;
        this.metaDataDTOPMapper = metaDataDTOPMapper;
        this.deleteFileService = deleteFileService;
    }

    public List<UploadedFileDTO> saveFile(CreateFileDTO createFileDTO, UUID folderUuid) {
        User user = userContext.getAuthenticatedUser();

        Folder targetFolder = resolveTargetFolder(createFileDTO.folderName(), folderUuid, user);

        return createFileDTO.files().stream()
                .map(file -> processAndSaveFile(file, targetFolder, folderUuid, user))
                .map(uploadFileDTOMapper)
                .toList();
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
        file.setDeletedAt(LocalDate.now());

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
        file.setDeletedAt(null);
        fileRepository.save(file);
        return uploadFileDTOMapper.apply(file);
    }

    public boolean deleteFilePermanently(UUID uuid) {
        UploadedFile file = fileRepository.findByUuid(uuid).orElse(null);
        return file != null && deleteFileService.delete(file);
    }

    public List<UploadedFileDTO> searchFilesByName(String searchTerm) {
        return fileRepository.findByName(searchTerm, userContext.getAuthenticatedUser())
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

        Filetype filetype = getFileType(file.getOriginalFilename());
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

    public int deleteAllDeletedFiles() {
        return (int) fileRepository.findAllDeletedFiles(userContext.getAuthenticatedUser())
                .stream()
                .map(UploadedFile::getUuid)
                .filter(this::deleteFilePermanently)
                .count();
    }

    public MetaDataDTO getMetaData(UUID uuid) {
        UploadedFile file = fileRepository.findByUuid(uuid).orElse(null);
        if (file == null) throw new IllegalArgumentException("File not found");
        return metaDataDTOPMapper.apply(file);
    }


    public Filetype getFileType(String fileName) {
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

    public void saveFileFromBytes(String name, byte[] content, UUID folderUuid) {
        // Just quietly return here
        if (name.isEmpty() || content.length == 0 || fileExists(name, folderUuid)) return;

        Filetype filetype = getFileType(name);
        if (filetype == null) return;

        String storedName = generateUniqueFileName(name);
        Path filePath = UPLOAD_DIR.resolve(storedName);
        try {
            Files.write(filePath, content);
        } catch (IOException e) {
            return;
        }
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setOriginalFilename(name);
        uploadedFile.setStoredName(storedName);
        uploadedFile.setSize((long) content.length);
        uploadedFile.setOwner(userContext.getAuthenticatedUser());
        uploadedFile.setStoragePath(filePath.toString());
        uploadedFile.setFiletype(filetype);
        uploadedFile.setDeleted(false);

        if (folderUuid != null) {
            uploadedFile.setFolder(folderService.findByUuid(folderUuid));
        } else if (folderUuid != null) {
            uploadedFile.setFolder(folderService.findByUuid(folderUuid));
        }
        fileRepository.save(uploadedFile);
    }

    private Folder resolveTargetFolder(String folderName, UUID folderUuid, User user) {
        if (folderName == null || folderName.trim().isEmpty()) {
            return null;
        }

        Folder folder = new Folder();
        folder.setName(folderName.trim());
        folder.setOwner(user);

        if (folderUuid != null) {
            folder.setParent(folderService.findByUuid(folderUuid));
        }

        return folderRepository.save(folder);
    }

    private UploadedFile processAndSaveFile(MultipartFile file, Folder folder, UUID folderUuid, User user) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || file.isEmpty()) {
            throw new IllegalArgumentException("File name is missing or file is empty");
        }

        if (fileExists(originalName, folderUuid)) {
            throw new IllegalArgumentException("File '" + originalName + "' already exists");
        }

        Filetype filetype = getFileType(file.getOriginalFilename());
        if (filetype == null) {
            throw new IllegalArgumentException("Unsupported or invalid file type for " + originalName);
        }

        String storedName = generateUniqueFileName(originalName);
        Path filePath = UPLOAD_DIR.resolve(storedName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalName + " on disk", e);
        }

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setOriginalFilename(originalName);
        uploadedFile.setStoredName(storedName);
        uploadedFile.setSize(file.getSize());
        uploadedFile.setOwner(user);
        uploadedFile.setStoragePath(filePath.toString());
        uploadedFile.setFiletype(filetype);
        uploadedFile.setDeleted(false);

        if (folder != null) {
            uploadedFile.setFolder(folder);
        } else if (folderUuid != null) {
            uploadedFile.setFolder(folderService.findByUuid(folderUuid));
        }

        return fileRepository.save(uploadedFile);
    }

    private String generateUniqueFileName(String originalFilename) {
        // Rename the file when storing it on the server to avoid name conflicts
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) extension = originalFilename.substring(dotIndex);

        return UUID.randomUUID().toString() + extension;
    }

    public void deleteFilesToBeDeleted(LocalDate threshold) {
        fileRepository.findAllByDeletedTrueAndDeletedAtBefore(threshold).forEach(file -> {
            // TODO: Just ignores unsuccessfully deleted files for now -> fix later
            deleteFileService.deleteFileOnMachine(file);
            fileRepository.delete(file);
        });
    }
}
