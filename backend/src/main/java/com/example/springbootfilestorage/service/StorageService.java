package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.storage.FileSizeDTO;
import com.example.springbootfilestorage.dto.storage.StorageDTO;
import com.example.springbootfilestorage.dto.storage.StorageDetailDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.repository.StorageRepository;
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

    public StorageService(StorageRepository storageRepository, FileRepository fileRepository, FolderRepository folderRepository) {
        this.storageRepository = storageRepository;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public StorageDTO getStorage() {
        Long usedSpaceVal = storageRepository.usedSpace();
        double totalSpace = 100;
        if (usedSpaceVal == null) return new StorageDTO(0, totalSpace);
        double usedSpace = usedSpaceVal.doubleValue();
        // TODO: specify total space somewhere
        return new StorageDTO(usedSpace, totalSpace);
    }

    public StorageDetailDTO getStorageDetails() {
        // TODO: This is very inefficient too many queries -> Fix later
        Long usedSpaceVal = storageRepository.usedSpace();
        double totalSpace = 100;
        double usedSpace;
        if (usedSpaceVal == null) usedSpace = 0;
        else usedSpace = usedSpaceVal.doubleValue();
        int numberOfFiles = fileRepository.numberOfFiles();
        int numberOfFolders = folderRepository.numberOfFolders();
        List<UploadedFile> fiveBiggestFiles = fileRepository.fiveBiggestFiles();
        return new StorageDetailDTO(
                usedSpace,
                totalSpace,
                numberOfFiles,
                numberOfFolders,
                fiveBiggestFiles.stream().map(this::createFileSizeDTO).toList()
        );
    }

    private FileSizeDTO createFileSizeDTO(UploadedFile file) {
        return new FileSizeDTO(file.getOriginalFilename(), file.getSize());
    }
}
