package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dto.FileNode;
import com.example.springbootfilestorage.dto.FolderNode;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.repository.FolderRepository;
import org.springframework.stereotype.Service;

@Service
public class FolderTreeService {

    private final FolderRepository folderRepository;

    public FolderTreeService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public FolderNode buildTree(Long rootFolderId) {
        Folder root = folderRepository.findById(rootFolderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        return buildNode(root);
    }

    private FolderNode buildNode(Folder folder) {
        FolderNode node = new FolderNode(folder.getName());

        for (Folder sub : folder.getSubfolders()) {
            node.getSubfolders().add(buildNode(sub));
        }

        for (UploadedFile file : folder.getFiles()) {
            node.getFiles().add(new FileNode(file.getOriginalFilename()));
        }

        return node;
    }
}

