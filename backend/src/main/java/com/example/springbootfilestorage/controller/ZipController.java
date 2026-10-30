package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.service.ZipService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/zip")
public class ZipController {

    private final ZipService zipService;

    public ZipController(ZipService zipService) {
        this.zipService = zipService;
    }

    @GetMapping(value = "/export/{uuid}", produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> exportZip(@PathVariable UUID uuid) {
        StreamingResponseBody body = out -> {
            try (ZipOutputStream zip = new ZipOutputStream(out)) {
                zipService.folderContentForZip(uuid).forEach((path, content) -> {
                    try {
                        zip.putNextEntry(new ZipEntry(path));
                        zip.write(content);
                        zip.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"export.zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(body);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importZip(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("No file uploaded");

        return null;
    }
}
