package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/deleted_files")
public class DeletedFileController {

    private final FileService fileService;

    public DeletedFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("")
    public ResponseEntity<List<UploadedFileDTO>> show() {
        return ResponseEntity.ok(fileService.findAllDeletedFiles());
    }

    @PostMapping("/restore/{uuid}")
    public ResponseEntity<UploadedFileDTO> restore(@PathVariable UUID uuid) {
        UploadedFileDTO restoredFile = fileService.restoreFile(uuid);
        System.out.println("No error");
        if (restoredFile == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(restoredFile);
    }

    @DeleteMapping("/delete/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid) {
        fileService.deleteFilePermanently(uuid);
    }
}
