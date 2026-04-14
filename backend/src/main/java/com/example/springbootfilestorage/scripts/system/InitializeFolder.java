package com.example.springbootfilestorage.scripts.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The InitializeFolder class is responsible for ensuring that a required folder structure
 * exists in the file system when the application starts. It implements the {@link CommandLineRunner}
 * interface to execute its logic during application initialization.
 * <p>
 * The folder path can be customized using the `spring.files.folder.custom.folder.path` configuration property.
 * If the custom folder path is not provided or is empty, a default folder named "file_storage_folder" is
 * created in the user's home directory. Otherwise, the specified custom folder path is initialized.
 * <p>
 * Functionality:
 * - Ensures that the default or custom folder exists.
 * - Creates the folder if it does not already exist.
 * - Logs the status of folder creation to the console.
 * <p>
 * Methods:
 * - `run(String... args)`: Entry point triggered during application startup to check and initialize the folder structure.
 * - `initializeFileFolder()`: Initializes the default folder in the user's home directory if no custom folder path is specified.
 * - `initializeCustomFileFolder()`: Initializes a folder at the specified custom path if provided.
 * <p>
 * This class relies on Java NIO for file and directory handling and provides console feedback for operations performed.
 */
@Component
public class InitializeFolder implements CommandLineRunner {

    @Value(value = "${spring.files.folder.custom.folder.path}")
    String customFolderPath;


    @Override
    public void run(String... args) throws Exception {
        if (customFolderPath == null || customFolderPath.isEmpty())
            initializeFileFolder();
        else
            initializeCustomFileFolder();
    }

    private void initializeCustomFileFolder() {
        Path customFolder = Paths.get(customFolderPath);
        if (!customFolder.toFile().exists())
            throw new IllegalArgumentException("Custom folder path does not exist: " + customFolderPath);
        else System.out.println("Custom folder exists at: " + customFolder.toAbsolutePath());
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
