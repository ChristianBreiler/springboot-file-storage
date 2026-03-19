package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.UploadedFileDTO;
import com.example.springbootfilestorage.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        UploadedFile file = fileService.findById(id);
        if (file == null) return ResponseEntity.notFound().build();
        try {
            Path path = Paths.get(file.getStoragePath());
            UrlResource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) return ResponseEntity.notFound().build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Show a file image for pngs in the browser
    @GetMapping("/show_image/{id}")
    public ResponseEntity<Resource> showFileImage(@PathVariable Long id) {
        UploadedFile file = fileService.findById(id);
        try {
            Path path = Paths.get(file.getStoragePath());
            UrlResource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) return ResponseEntity.notFound().build();

            String contentType = Files.probeContentType(path);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/open/{id}")
    public ResponseEntity<Resource> openFile(@PathVariable Long id) throws MalformedURLException {
        UploadedFile file = fileService.findById(id);
        Path path = Paths.get(file.getStoragePath());
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        String contentType = file.probeContentType();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping(
            value = {"/upload", "/upload/{folderId}"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UploadedFileDTO> upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable(required = false) Long folderId) {
        if (file.isEmpty()) return ResponseEntity.badRequest().build();

        UploadedFileDTO updatedFile = fileService.saveFile(file, folderId);
        if (updatedFile == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(updatedFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UploadedFileDTO> show(@PathVariable Long id) {
        UploadedFileDTO file = fileService.findDTOById(id);
        if (file == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<UploadedFileDTO>(file, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        fileService.deleteFile(id);
    }

    @PostMapping("/rename/{id}")
    public ResponseEntity<UploadedFile> rename(@PathVariable Long id, @RequestParam String newName) {
        UploadedFile updatedFile = fileService.renameFile(id, newName);
        return ResponseEntity.ok(updatedFile);
    }

    // Only allow files up to 1GB
    private boolean fileTooBig(MultipartFile file) {
        return file.getSize() > 1073741824;
    }
}
