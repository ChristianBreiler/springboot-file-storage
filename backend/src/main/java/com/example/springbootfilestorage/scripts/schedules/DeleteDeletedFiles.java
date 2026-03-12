package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.service.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DeleteDeletedFiles {

    private final FileService fileService;

    public DeleteDeletedFiles(FileService fileService) {
        this.fileService = fileService;
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void deleteDeletedFiles() {
        fileService.deleteDeletedFiles();
    }
}
