package com.example.springbootfilestorage.scripts.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * InitializeFolder is a Spring Component that implements CommandLineRunner.
 * It is responsible for creating and initializing specific folders in the user's home directory
 * upon application startup.
 * <p>
 * The class ensures that a root folder named "file_storage_folder" exists,
 * and within it, another folder named "profile_pics" is created.
 * If these folders already exist, it logs their locations.
 * <p>
 * If the root folder "file_storage_folder" does not exist during the initialization
 * of "profile_pics", a runtime exception is thrown.
 * <p>
 * This functionality is triggered during the application startup phase.
 */
@Component
public class InitializeFolder implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        initializeFileFolder();
        initializeProfilePicFolder();
    }

    private void initializeFileFolder() throws IOException {
        String homeDir = System.getProperty("user.home");
        Path storageFolder = Paths.get(homeDir, "file_storage_folder");
        if (!storageFolder.toFile().exists()) {
            Files.createDirectories(storageFolder);
            System.out.println("file_storage_folder created at: " + storageFolder.toAbsolutePath());
        } else System.out.println("file_storage_folder exists at: " + storageFolder.toAbsolutePath());
    }

    private void initializeProfilePicFolder() throws IOException {
        String homeDir = System.getProperty("user.home");
        Path storageFolder = Paths.get(homeDir, "file_storage_folder");
        if (!storageFolder.toFile().exists()) throw new RuntimeException("file_storage_folder does not exist");
        Path profilePicFolder = storageFolder.resolve("profile_pics");
        if (!profilePicFolder.toFile().exists()) {
            Files.createDirectories(profilePicFolder);
            System.out.println("profile_pics created at: " + profilePicFolder.toAbsolutePath());
        } else System.out.println("profile_pics exists at: " + profilePicFolder.toAbsolutePath());
    }
}
