package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.FolderDTO;
import com.example.springbootfilestorage.dto.SearchResultDTO;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.scripts.view_beans.SettingsViewBean;
import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderService folderService;
    private final FileService fileService;
    private final int MAX_PARENT_FOLDERS = 5;
    private final SettingsViewBean settingsViewBean;

    public FolderController(FolderService folderService, FileService fileService, SettingsViewBean settingsViewBean) {
        this.folderService = folderService;
        this.fileService = fileService;
        this.settingsViewBean = settingsViewBean;
    }

    // Retrieves the home folder details for the current user
    @GetMapping("/home")
    public ResponseEntity<FolderDTO> home() {
        return new ResponseEntity<FolderDTO>(folderService.findHomeDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderDTO> show(@PathVariable Long id) {
        return new ResponseEntity<FolderDTO>(folderService.findByDTOId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/search")
    public ResponseEntity<SearchResultDTO> searchByName(@RequestParam(required = true) String name, @PathVariable Long id) {
        List<Folder> filteredSubfolders = folderService.searchFoldersByName(id, name);
        List<UploadedFile> filteredSubfiles = fileService.searchFilesByName(id, name);
        return new ResponseEntity<SearchResultDTO>(new SearchResultDTO(filteredSubfolders, filteredSubfiles),
                HttpStatus.OK);
    }

    @PostMapping("/rename/{id}")
    public ResponseEntity<Folder> renameFolder(@PathVariable Long id, @RequestParam String newName) {
        Folder updatedFolder = folderService.renameFolder(id, newName);
        return ResponseEntity.ok(updatedFolder);
    }

    @DeleteMapping("/deleteFolder/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void deleteFolderHome(@PathVariable Long id) {
        folderService.deleteFolder(id);
    }

    @PostMapping({"/createfolder", "/createfolder/{folderId}"})
    public ResponseEntity<Folder> createFolder(@RequestParam String folderName,
                                               @PathVariable(required = false) Long folderId) {
        Folder updatedFolder = folderService.saveFolder(folderName, folderId);
        return ResponseEntity.ok(updatedFolder);
    }
}
