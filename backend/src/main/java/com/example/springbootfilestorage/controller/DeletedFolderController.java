package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;
import com.example.springbootfilestorage.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/deleted_folders")
public class DeletedFolderController {

    private final FolderService folderService;

    public DeletedFolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("")
    public ResponseEntity<List<FolderSummaryDTO>> show() {
        return ResponseEntity.ok(folderService.findAllDeletedFolders());
    }

    @PostMapping("/restore/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restore(@PathVariable UUID uuid) {
        folderService.restoreFolder(uuid);
    }

    @DeleteMapping("/delete/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid) {
        folderService.deleteFolderPermanently(uuid);
    }
}
