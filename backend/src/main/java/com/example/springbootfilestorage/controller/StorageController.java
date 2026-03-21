package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.storage.StorageDTO;
import com.example.springbootfilestorage.dto.storage.StorageDetailDTO;
import com.example.springbootfilestorage.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("")
    public ResponseEntity<StorageDTO> storage() {
        StorageDTO storage = storageService.getStorage();
        if (storage == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(storage);
    }

    @GetMapping("/details")
    public ResponseEntity<StorageDetailDTO> storageDetails() {
        StorageDetailDTO storage = storageService.getStorageDetails();
        if (storage == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(storage);
    }
}
