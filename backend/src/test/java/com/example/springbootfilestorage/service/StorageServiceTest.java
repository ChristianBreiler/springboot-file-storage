package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.storage.FileSizeDTO;
import com.example.springbootfilestorage.dto.storage.StorageDTO;
import com.example.springbootfilestorage.dto.storage.StorageDetailDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.repository.StorageRepository;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    private static final long ONE_MB = 1024L * 1024L;

    @Mock
    private StorageRepository storageRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private FolderRepository folderRepository;
    @Mock
    private UserContext userContext;

    private StorageService storageService;
    private User user;

    @BeforeEach
    void setUp() {
        storageService = new StorageService(storageRepository, fileRepository, folderRepository, userContext);

        user = new User();
        user.setUuid(UUID.randomUUID());
        when(userContext.getAuthenticatedUser()).thenReturn(user);
    }

    @Test
    void getStorage_convertsUsedBytesToMB() {
        when(storageRepository.usedSpace(user)).thenReturn(5 * ONE_MB / 2);

        StorageDTO result = storageService.getStorage();

        assertThat(result.getUsedSpace()).isEqualTo(2.5);
        assertThat(result.getTotalSpace()).isEqualTo(100.0);
    }

    @Test
    void getStorage_nullUsedSpace_returnsZero() {
        when(storageRepository.usedSpace(user)).thenReturn(null);

        StorageDTO result = storageService.getStorage();

        assertThat(result.getUsedSpace()).isZero();
    }

    @Test
    void getStorageDetails_aggregatesCountsAndBiggestFiles() {
        when(storageRepository.usedSpace(user)).thenReturn(10 * ONE_MB);
        when(fileRepository.numberOfFiles()).thenReturn(7);
        when(folderRepository.numberOfFolders()).thenReturn(3);
        when(fileRepository.fiveBiggestFiles(user)).thenReturn(List.of(
                file("video.mp3", 4 * ONE_MB),
                file("scan.pdf", ONE_MB / 2)
        ));

        StorageDetailDTO result = storageService.getStorageDetails();

        assertThat(result.getUsedSpace()).isEqualTo(10.0);
        assertThat(result.getTotalSpace()).isEqualTo(1000.0);
        assertThat(result.getNumberOfFiles()).isEqualTo(7);
        assertThat(result.getNumberOfFolders()).isEqualTo(3);
        assertThat(result.getFiveLargestFiles()).containsExactly(
                new FileSizeDTO("video.mp3", 4.0),
                new FileSizeDTO("scan.pdf", 0.5)
        );
    }

    @Test
    void getStorageDetails_fileWithNullSize_reportsZero() {
        when(storageRepository.usedSpace(user)).thenReturn(0L);
        when(fileRepository.fiveBiggestFiles(user)).thenReturn(List.of(file("empty.pdf", null)));

        StorageDetailDTO result = storageService.getStorageDetails();

        assertThat(result.getFiveLargestFiles()).containsExactly(new FileSizeDTO("empty.pdf", 0));
    }

    private UploadedFile file(String name, Long size) {
        UploadedFile file = new UploadedFile();
        file.setUuid(UUID.randomUUID());
        file.setOriginalFilename(name);
        file.setSize(size);
        return file;
    }
}
