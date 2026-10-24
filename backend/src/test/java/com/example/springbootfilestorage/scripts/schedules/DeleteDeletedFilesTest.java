package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteDeletedFilesTest {

    @Mock
    private FileRepository fileRepository;

    @Test
    void deleteDeletedFiles_deletesFilesOlderThanConfiguredWeeks() {
        DeleteDeletedFiles job = new DeleteDeletedFiles(fileRepository);
        ReflectionTestUtils.setField(job, "deleteAfterWeeks", 4);

        job.deleteDeletedFiles();

        verify(fileRepository).deleteAllByDeletedTrueAndDeletedAtBefore(LocalDate.now().minusWeeks(4));
    }
}
