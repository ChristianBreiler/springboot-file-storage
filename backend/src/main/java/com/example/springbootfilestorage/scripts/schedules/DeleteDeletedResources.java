package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.FolderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

// Schedule that automatically deletes all resources that are marked as "deleted" and their date is due

@Service
public class DeleteDeletedResources {

    private final FileService fileService;
    private final FolderService folderService;

    @Value("${spring.files.delete.after.weeks}")
    private int deleteAfterWeeks;

    public DeleteDeletedResources(FileService fileService, FolderService folderService) {
        this.fileService = fileService;
        this.folderService = folderService;
    }


    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteDeletedResources() {
        LocalDate threshold = LocalDate.now().minusWeeks(deleteAfterWeeks);
        fileService.deleteFilesToBeDeleted(threshold);
        folderService.deleteFoldersToBeDeleted(threshold);
    }
}
