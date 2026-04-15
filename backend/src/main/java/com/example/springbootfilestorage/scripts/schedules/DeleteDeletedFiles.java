package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

// Schedule that automatically deletes all files that are marked as "deleted" and their date is due

@Service
public class DeleteDeletedFiles {

    private final FileRepository fileRepository;

    @Value("${spring.files.delete.after.weeks}")
    private int deleteAfterWeeks;

    public DeleteDeletedFiles(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteDeletedFiles() {
        LocalDate threshold = LocalDate.now().minusWeeks(deleteAfterWeeks);
        fileRepository.deleteAllByDeletedTrueAndDeletedAtBefore(threshold);
    }
}
