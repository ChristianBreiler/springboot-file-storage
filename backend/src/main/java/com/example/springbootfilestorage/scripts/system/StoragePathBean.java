package com.example.springbootfilestorage.scripts.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the path to the folder where files are stored and all its subfolders
 */
@Component
public class StoragePathBean {

    @Value(value = "${spring.files.folder.custom.folder.path}")
    String customFolderPath;

    private final Path storageFolder;

    public StoragePathBean() {
        if (customFolderPath == null || customFolderPath.isEmpty()) {
            String homeDir = System.getProperty("user.home");
            this.storageFolder = Paths.get(homeDir, "file_storage_folder");
        } else {
            this.storageFolder = Paths.get(customFolderPath)    ;
        }
    }

    public Path getStorageFolderPath() {
        return storageFolder;
    }
}
