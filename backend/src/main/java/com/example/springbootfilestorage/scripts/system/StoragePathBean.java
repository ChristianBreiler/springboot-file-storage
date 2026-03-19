package com.example.springbootfilestorage.scripts.system;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the path to the folder where files are stored and all its subfolders
 */
@Component
public class StoragePathBean {

    private final Path storageFolder;
    private final Path profilePicFolder;

    public StoragePathBean() {
        String homeDir = System.getProperty("user.home");
        this.storageFolder = Paths.get(homeDir, "file_storage_folder");
        this.profilePicFolder = Paths.get(homeDir, "file_storage_folder", "profile_pics");
    }

    public Path getStorageFolderPath() {
        return storageFolder;
    }

    public Path getProfilePicFolderPath() {
        return profilePicFolder;
    }
}
