package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.MoveFileDto;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.FolderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller for the drag and drop functionality on the server side.
@RequestMapping("/drag")
@RestController
public class DragController {

    private final FileService fileService;
    private final FolderService folderService;

    public DragController(FileService fileService, FolderService folderService) {
        this.fileService = fileService;
        this.folderService = folderService;
    }

    @PostMapping("/move-file")
    public FolderDTO moveFileIntoFolder(@RequestBody MoveFileDto moveFileDto) {
        return fileService.moveFileToFolder(moveFileDto);
    }
}