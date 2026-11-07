package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.FolderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class DeleteDeletedResourcesTest {

    @Mock
    private FileService fileService;

    @Mock
    private FolderService folderService;

    @Test
    void deleteDeletedResources_deletesFilesAndFoldersOlderThanConfiguredWeeks() {
        DeleteDeletedResources job = new DeleteDeletedResources(fileService, folderService);
        ReflectionTestUtils.setField(job, "deleteAfterWeeks", 4);

        job.deleteDeletedResources();

        LocalDate threshold = LocalDate.now().minusWeeks(4);
        InOrder order = inOrder(fileService, folderService);
        order.verify(fileService).deleteFilesToBeDeleted(threshold);
        order.verify(folderService).deleteFoldersToBeDeleted(threshold);
    }
}
