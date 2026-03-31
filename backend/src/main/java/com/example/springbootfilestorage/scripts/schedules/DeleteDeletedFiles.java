package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.service.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Schedule that automatically deletes all files that are marked as "deleted" and their date is due

@Service
public class DeleteDeletedFiles {

    private final FileService fileService;
    private final FileRepository fileRepository;

    public DeleteDeletedFiles(FileService fileService, FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void deleteDeletedFiles() {
        List<UploadedFile> files = fileRepository.findAllFilesToBeDeletedToday(LocalDate.now());
        files.forEach(f -> fileService.deleteFilePermanently(f.getUuid()));
    }
}
