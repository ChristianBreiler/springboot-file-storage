package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.folder.CanDeleteFolderDTO;
import com.example.springbootfilestorage.dto.folder.CreateFolderDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.folder.RenameFolderDTO;
import com.example.springbootfilestorage.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/home")
    public ResponseEntity<FolderDTO> home() {
        return ResponseEntity.ok(folderService.findHomeDTO());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<FolderDTO> show(@PathVariable UUID uuid) {
        return ResponseEntity.ok(folderService.findByDTOUuid(uuid));
    }

    @PostMapping("/rename/{uuid}")
    public ResponseEntity<FolderDTO> rename(@RequestBody RenameFolderDTO renameFolderDTO, @PathVariable UUID uuid) {
        if (renameFolderDTO.newFolderName().trim().isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(folderService.renameFolder(uuid, renameFolderDTO));
    }

    @DeleteMapping("/delete/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void deleteFolderHome(@PathVariable UUID uuid) {
        folderService.deleteFolder(uuid);
    }

    @PostMapping({"/create", "/create/{folderUuid}"})
    public ResponseEntity<FolderDTO> create(@RequestBody CreateFolderDTO createFolderDTO,
                                            @PathVariable(required = false) UUID folderUuid) {
        return ResponseEntity.ok(folderService.saveFolder(createFolderDTO, folderUuid));
    }

    @GetMapping("/delete_folder_info/{uuid}")
    public ResponseEntity<CanDeleteFolderDTO> deleteFolderInfo(@PathVariable UUID uuid) {
        // Shows the user if the folder is empty and if it can therefore be deleted
        return ResponseEntity.ok(folderService.canFolderBeDeleted(uuid));
    }
}
