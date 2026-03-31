package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.storage.FileSizeDTO;
import com.example.springbootfilestorage.dto.storage.StorageDTO;
import com.example.springbootfilestorage.dto.storage.StorageDetailDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.repository.StorageRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for handling storage-related operations.
 * It provides methods to retrieve storage usage information and details.
 */

@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final UserContext userContext;

    public StorageService(StorageRepository storageRepository, FileRepository fileRepository,
                          FolderRepository folderRepository, UserContext userContext) {
        this.storageRepository = storageRepository;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.userContext = userContext;
    }

    public StorageDTO getStorage() {
        double usedSpace = usedSpaceInMB();
        double totalSpaceGB = 100.0;

        return new StorageDTO(usedSpace, totalSpaceGB);
    }

    public StorageDetailDTO getStorageDetails() {
        double usedSpace = usedSpaceInMB();
        // In MB
        double totalSpace = 1000;
        int numberOfFiles = fileRepository.numberOfFiles();
        int numberOfFolders = folderRepository.numberOfFolders();
        List<UploadedFile> fiveBiggestFiles = fileRepository.fiveBiggestFiles(userContext.getAuthenticatedUser());
        return new StorageDetailDTO(
                usedSpace,
                totalSpace,
                numberOfFiles,
                numberOfFolders,
                fiveBiggestFiles.stream().map(this::createFileSizeDTO).toList()
        );
    }

    private FileSizeDTO createFileSizeDTO(UploadedFile file) {
        return new FileSizeDTO(file.getOriginalFilename(), convertToMB(file.getSize()));
    }

    private double usedSpaceInMB() {
        return convertToMB(storageRepository.usedSpace(userContext.getAuthenticatedUser()));
    }

    private double convertToMB(Long bytes) {
        return bytes == null ? 0 : bytes / (1024.0 * 1024.0);
    }
}
