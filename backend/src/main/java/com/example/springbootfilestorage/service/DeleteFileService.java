package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DeleteFileService {

    // Make this a unique service to avoid cyclical conflicts with folder and file service

    private final FileRepository fileRepository;

    public DeleteFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public boolean delete(UploadedFile file) {
        if (file == null || !deleteFileOnMachine(file.getStoragePath())) return false;
        fileRepository.delete(file);
        return true;
    }

    // Only removes the physical file, the database entry is left to the caller (e.g. cascading folder deletion)
    public boolean deleteFileOnMachine(UploadedFile file) {
        return file != null && deleteFileOnMachine(file.getStoragePath());
    }

    private boolean deleteFileOnMachine(String filepath) {
        try {
            Path path = Paths.get(filepath);
            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
