package com.example.springbootfilestorage.scripts.system;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the path to the folder where files are stored and all its subfolders
 */
@Component
public class StoragePathBean {

    public Path getStorageFolderPath() {
        String homeDir = System.getProperty("user.home");
        return Paths.get(homeDir, "file_storage_folder");
    }

    public Path getProfilePicFolderPath() {
        String homeDir = System.getProperty("user.home");
        return Paths.get(homeDir, "file_storage_folder", "profile_pics");
    }
}
