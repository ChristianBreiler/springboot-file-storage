package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.foldertree.FolderNodeDTO;
import com.example.springbootfilestorage.service.FolderTreeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foldertree")
public class FolderTreeController {

    private final FolderTreeService folderTreeService;

    public FolderTreeController(FolderTreeService folderTreeService) {
        this.folderTreeService = folderTreeService;
    }

    @GetMapping
    public ResponseEntity<FolderNodeDTO> getFolderTree() {
        return ResponseEntity.ok(folderTreeService.buildTree());
    }
}
