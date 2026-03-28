package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.folder.CreateFolderDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.folder.CanDeleteFolderDTO;
import com.example.springbootfilestorage.dto.folder.RenameFolderDTO;
import com.example.springbootfilestorage.dto.search.SearchResultDTO;
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
    private final SettingsViewBean settingsViewBean;

    public FolderController(FolderService folderService, FileService fileService, SettingsViewBean settingsViewBean) {
        this.folderService = folderService;
        this.fileService = fileService;
        this.settingsViewBean = settingsViewBean;
    }

    @GetMapping("/home")
    public ResponseEntity<FolderDTO> home() {
        return ResponseEntity.ok(folderService.findHomeDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderDTO> show(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.findByDTOId(id));
    }

    @GetMapping("/{id}/search")
    public ResponseEntity<SearchResultDTO> searchByName(@RequestParam(required = true) String name, @PathVariable Long id) {
        List<Folder> filteredSubfolders = folderService.searchFoldersByName(id, name);
        List<UploadedFile> filteredSubfiles = fileService.searchFilesByName(id, name);
        return new ResponseEntity<SearchResultDTO>(new SearchResultDTO(filteredSubfolders, filteredSubfiles),
                HttpStatus.OK);
    }

    @PostMapping("/rename/{id}")
    public ResponseEntity<FolderDTO> renameFolder(@RequestBody RenameFolderDTO renameFolderDTO, @PathVariable Long id) {
        if (renameFolderDTO.newFolderName().trim().isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(folderService.renameFolder(id, renameFolderDTO));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void deleteFolderHome(@PathVariable Long id) {
        folderService.deleteFolder(id);
    }

    @PostMapping({"/create", "/create/{folderId}"})
    public ResponseEntity<FolderDTO> createFolder(@RequestBody CreateFolderDTO createFolderDTO,
                                                  @PathVariable(required = false) Long folderId) {
        return ResponseEntity.ok(folderService.saveFolder(createFolderDTO, folderId));
    }

    @GetMapping("/delete_folder_info/{id}")
    public ResponseEntity<CanDeleteFolderDTO> deleteFolderInfo(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.canFolderBeDeleted(id));
    }
}
