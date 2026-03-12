package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.scripts.system.StoragePathBean;
import com.example.springbootfilestorage.service.MessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class FileCleanupJob {

    private final FileRepository fileRepository;
    private final StoragePathBean storagePathBean;
    private final MessageService messageService;
    List<String> messages;

    public FileCleanupJob(FileRepository fileRepository, StoragePathBean storagePathBean, MessageService messageService) {
        this.fileRepository = fileRepository;
        this.storagePathBean = storagePathBean;
        this.messageService = messageService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupFiles() {
        messages = new ArrayList<>();
        System.out.println("Cleaning up files...");

        cleanUpIncorrectFilepaths();
        cleanUpFilesWithNoPath();

        // Get messages of deleted files and store them for the admin
        if (messages.isEmpty()) System.out.println("No files deleted");
        else messages.forEach(message -> {
            System.out.println(message);
            messageService.saveMessage(message);
        });
        messages.clear();
    }

    // Delete files with no physical file on disk (e.g., deleted by the user)
    private void cleanUpFilesWithNoPath() {
        String folderPath = storagePathBean.getStorageFolderPath().toString();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            List<File> files = Arrays.asList(Objects.requireNonNull(folder.listFiles()));
            files = files.stream().filter(File::isFile).toList();

            Path storageFolderPath = storagePathBean.getStorageFolderPath();
            files.forEach(f -> {
                Path filePath = Paths.get(f.getAbsolutePath());
                if (!fileRepository.physicalFileExists(filePath.toString()) && !filePath.startsWith(storageFolderPath)) {
                    messages.add("Deleted file: " + f.getAbsolutePath() + " (no physical file on disk)");
                    f.delete();
                }
            });
        }
    }

    // Delete files with incorrect filepaths (e.g., deleted by the user)
    private void cleanUpIncorrectFilepaths() {
        List<UploadedFile> files = fileRepository.findAll();
        files.forEach(file -> {
            if (file.getStoragePath() == null) {
                fileRepository.delete(file);
            } else {
                File f = new File(file.getStoragePath());
                if (!f.exists()) {
                    fileRepository.delete(file);
                    messages.add("Deleted file: " + f.getAbsolutePath() + " (no physical file on disk)");
                }
            }
        });
    }
}
