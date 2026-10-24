package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.metadata.MetaDataDTO;
import com.example.springbootfilestorage.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/metadata")
public class MetaDataController {

    private final FileService fileService;

    public MetaDataController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MetaDataDTO> show(@PathVariable UUID uuid) {
        return ResponseEntity.ok(fileService.getMetaData(uuid));
    }
}
