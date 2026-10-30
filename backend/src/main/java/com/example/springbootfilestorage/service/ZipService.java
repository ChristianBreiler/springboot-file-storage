package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.repository.FolderRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZipService {

    private final FolderRepository folderRepository;

    public ZipService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Map<String, byte[]> folderContentForZip(UUID uuid) {
        Folder folder = folderRepository.findByUuid(uuid).orElse(null);
        if (folder == null) return Map.of();

        return fileContentForFolder(folder);
    }

    private Map<String, byte[]> fileContentForFolder(Folder folder) {
        Map<String, byte[]> folderContent = new HashMap<>();
        List<Folder> chain = new ArrayList<>(folder.allParents());
        Collections.reverse(chain);
        chain.add(folder);
        String path = chain.stream().map(Folder::getName).collect(Collectors.joining("/"));

        for (UploadedFile file : folder.getFiles()) {
            if (file.isDeleted()) continue;
            try {
                folderContent.put(path + "/" + file.getOriginalFilename(),
                        Files.readAllBytes(Path.of(file.getStoragePath())));
            } catch (IOException e) {
                throw new UncheckedIOException("Could not read " + file.getStoragePath(), e);
            }
        }
        for (Folder subfolder : folder.getSubfolders()) {
            folderContent.putAll(fileContentForFolder(subfolder));
        }
        return folderContent;
    }
}
