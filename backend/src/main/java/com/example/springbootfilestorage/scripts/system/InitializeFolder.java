package com.example.springbootfilestorage.scripts.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A Spring Boot component that initializes a file storage folder during the application startup.
 * Implements the {@link CommandLineRunner} interface to execute the folder
 * initialization process after the Spring Boot application has started.
 * <p>
 * This class checks if a specific folder (by default, located in the user's home directory and named
 * "file_storage_folder") exists. If the folder does not exist, it creates the folder. If the folder already
 * exists, it logs its location.
 * <p>
 * The folder created or accessed via this component is typically used for file storage purposes in
 * the application.
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
