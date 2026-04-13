package com.example.springbootfilestorage.scripts.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The InitializeFolder class is a Spring Component that implements CommandLineRunner.
 * It is used to ensure the existence of a specific folder in the user's home directory
 * upon application startup. If the folder does not exist, it will be created.
 * <p>
 * The folder created or referenced is named "file_storage_folder".
 * This functionality is intended to set up the storage directory required
 * for other components of the application to function properly.
 * <p>
 * The run method, inherited from CommandLineRunner, is automatically executed
 * by the Spring Boot framework during application startup.
 * <p>
 * Methods:
 * - run(String... args): This method invokes the initialization logic to ensure
 * the presence of the storage folder. It delegates the operation to a private method.
 * - initializeFileFolder(): A private method responsible for checking the existence
 * of the storage folder and creating it if it does not already exist.
 */
@Component
public class InitializeFolder implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        initializeFileFolder();
    }

    private void initializeFileFolder() throws IOException {
        String homeDir = System.getProperty("user.home");
        Path storageFolder = Paths.get(homeDir, "file_storage_folder");
        if (!storageFolder.toFile().exists()) {
            Files.createDirectories(storageFolder);
            System.out.println("file_storage_folder created at: " + storageFolder.toAbsolutePath());
        } else System.out.println("file_storage_folder exists at: " + storageFolder.toAbsolutePath());
    }
}
