package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.folder.CreateFolderDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
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
    private final FolderService folderService;
    private final FileService fileService;

    public ZipService(FolderRepository folderRepository, FolderService folderService, FileService fileService) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.fileService = fileService;
    }

    public Map<String, byte[]> folderContentForZip(UUID uuid) {
        Folder folder = folderRepository.findByUuid(uuid).orElse(null);
        if (folder == null) return Map.of();

        return fileContentForFolder(folder);
    }

    public void addContentFromZip(List<String> folderNames, Map<String, byte[]> fileMap, UUID partentUuid) {
        Map<String, byte[]> normalizedFileMap = new HashMap<>();

        fileMap.forEach((path, content) -> {
            path = normalizePath(path);
            if (!skipThis(path)) normalizedFileMap.put(path, content);
        });

        List<String> folderSources = new ArrayList<>(normalizedFolderNames(folderNames));
        normalizedFileMap.keySet().stream()
                .map(this::parentPath)
                .filter(Objects::nonNull)
                .forEach(folderSources::add);

        List<String> allPaths = getAllPaths(folderSources);

        Map<String, UUID> folderMap = new HashMap<>();
        for (String folderName : allPaths) {
            if (skipThis(folderName)) continue;
            String parentPath = parentPath(folderName);
            FolderDTO fdto = folderService.saveFolder(
                    new CreateFolderDTO(folderName.substring(folderName.lastIndexOf('/') + 1)),
                    parentPath == null ? partentUuid : folderMap.get(parentPath));
            folderMap.put(folderName, fdto.uuid());
        }

        normalizedFileMap.forEach((path, content) -> {
            String parent = parentPath(path);
            UUID folderUuid = parent == null ? partentUuid : folderMap.get(parent);
            String name = path.substring(path.lastIndexOf('/') + 1);
            if (fileService.getFileType(name) == null) return;
            fileService.saveFileFromBytes(name, content, folderUuid);
        });
    }

    private List<String> normalizedFolderNames(List<String> folderNames) {
        return folderNames.stream()
                .map(f -> f.replaceAll("\\\\", "/").replaceFirst("^/+", ""))
                .collect(Collectors.toList());
    }

    private String normalizePath(String path) {
        String normalized = path.replaceAll("\\\\", "/").replaceFirst("^/+", "");
        if (Arrays.asList(normalized.split("/")).contains(".."))
            throw new IllegalArgumentException("Invalid path in zip: " + path);
        return normalized;
    }

    private boolean skipThis(String path) {
        return path.startsWith("__MACOSX") ||
                (path.substring(path.lastIndexOf('/') + 1)).equals(".DS_Store");
    }

    private String parentPath(String path) {
        int i = path.lastIndexOf('/');
        return i < 0 ? null : path.substring(0, i);
    }


    private List<String> getAllPaths(List<String> folderNames) {
        List<String> allPaths = new ArrayList<>();

        for (String folderName : folderNames) {
            String[] split = folderName.split("/");
            StringBuilder buffer = new StringBuilder();

            for (String s : split) {
                if (s.isEmpty()) continue;

                if (buffer.isEmpty()) buffer.append(s);
                else buffer.append("/").append(s);

                if (!allPaths.contains(buffer.toString())) allPaths.add(buffer.toString());
            }
        }
        allPaths.sort(Comparator.comparingInt(s -> (int) s.chars().filter(ch -> ch == '/').count()));
        return allPaths;
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
