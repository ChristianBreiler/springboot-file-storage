package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.UploadedFileDTO;
import com.example.springbootfilestorage.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/deleted_files")
public class DeletedFileController {

    private final FileService fileService;

    public DeletedFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("")
    public ResponseEntity<List<UploadedFileDTO>> showDeletedFiles() {
        return new ResponseEntity<>(fileService.findAllDeletedFiles(), HttpStatus.OK);
    }

    @PostMapping("/restore_file/{id}")
    public ResponseEntity<UploadedFileDTO> restoreFile(@PathVariable Long id) {
        UploadedFileDTO restoredFile = fileService.restoreFile(id);
        if (restoredFile == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(restoredFile);
    }

    @DeleteMapping("/delete_file_permanently/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilePermanently(@PathVariable Long id) {
        fileService.deleteFilePermanently(id);
    }
}
