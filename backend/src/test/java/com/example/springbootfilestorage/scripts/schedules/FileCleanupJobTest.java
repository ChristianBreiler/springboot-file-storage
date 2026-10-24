package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.scripts.system.StoragePathBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileCleanupJobTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private StoragePathBean storagePathBean;

    @TempDir
    Path tempDir;

    private FileCleanupJob job;

    @BeforeEach
    void setUp() {
        job = new FileCleanupJob(fileRepository, storagePathBean);
        when(storagePathBean.getStorageFolderPath()).thenReturn(tempDir);
    }

    @Test
    void cleanupFiles_deletesRecordWithoutStoragePath() {
        UploadedFile noPath = file(null);
        when(fileRepository.findAll()).thenReturn(List.of(noPath));

        job.cleanupFiles();

        verify(fileRepository).delete(noPath);
    }

    @Test
    void cleanupFiles_deletesRecordWhosePhysicalFileIsMissing() {
        UploadedFile missing = file(tempDir.resolve("missing.pdf").toString());
        when(fileRepository.findAll()).thenReturn(List.of(missing));

        job.cleanupFiles();

        verify(fileRepository).delete(missing);
    }

    @Test
    void cleanupFiles_keepsRecordAndFileWhenBothExist() throws IOException {
        Path stored = Files.writeString(tempDir.resolve("stored.pdf"), "content");
        UploadedFile existing = file(stored.toString());
        when(fileRepository.findAll()).thenReturn(List.of(existing));
        lenient().when(fileRepository.physicalFileExists(stored.toString())).thenReturn(true);

        job.cleanupFiles();

        verify(fileRepository, never()).delete(any(UploadedFile.class));
        assertThat(stored).exists();
    }

    @Test
    void cleanupFiles_missingStorageFolder_onlyCleansRecords() {
        when(storagePathBean.getStorageFolderPath()).thenReturn(tempDir.resolve("does-not-exist"));
        when(fileRepository.findAll()).thenReturn(List.of());

        job.cleanupFiles();

        verify(fileRepository, never()).physicalFileExists(any());
        verify(fileRepository, never()).delete(any(UploadedFile.class));
    }

    private UploadedFile file(String storagePath) {
        UploadedFile file = new UploadedFile();
        file.setUuid(UUID.randomUUID());
        file.setStoragePath(storagePath);
        return file;
    }
}
