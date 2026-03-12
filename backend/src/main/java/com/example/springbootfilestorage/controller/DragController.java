package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.MoveFileDto;
import com.example.springbootfilestorage.dto.MoveFolderDto;
import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.FolderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Controller for the drag and drop functionality on the server side.
@RestController
public class DragController {

    private final FileService fileService;
    private final FolderService folderService;

    public DragController(FileService fileService, FolderService folderService) {
        this.fileService = fileService;
        this.folderService = folderService;
    }

    @PostMapping("/move-file")
    public void moveFileIntoFolder(@RequestBody MoveFileDto moveFileDto) {
        Long fileId = moveFileDto.getFileId();
        Long folderId = moveFileDto.getContainerId();
        fileService.moveFileToFolder(fileId, folderId);
    }

    // TODO: Implement this
    @PostMapping("/move-folder")
    public void moveFolderIntoFolder(@RequestBody MoveFolderDto moveFolderDto) {
        Long folderId = moveFolderDto.getContainerId();
        long folderTargetId = moveFolderDto.getContainerTargetId();
        folderService.moveFolderToFolder(folderId, folderTargetId);

    }
}