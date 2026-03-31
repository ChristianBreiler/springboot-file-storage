package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.file.CreateFileDTO;
import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("")
    public ResponseEntity<List<UploadedFileDTO>> index() {
        return ResponseEntity.ok(fileService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UploadedFileDTO> show(@PathVariable UUID uuid) {
        UploadedFileDTO file = fileService.findDTOByUuid(uuid);
        if (file == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(file);
    }

    @PostMapping(value = {"/upload", "/upload/{folderUuid}"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadedFileDTO> upload(@ModelAttribute CreateFileDTO createFileDTO,
                                                  @PathVariable(required = false) UUID folderUuid) {
        if (createFileDTO == null || createFileDTO.file().isEmpty()) return ResponseEntity.badRequest().build();

        UploadedFileDTO file = fileService.saveFile(createFileDTO, folderUuid);
        if (file == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(file);
    }

    @GetMapping("/download/{uuid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID uuid) {
        UploadedFile file = fileService.findByUuid(uuid);
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
    @GetMapping("/show_image/{uuid}")
    public ResponseEntity<Resource> showFileImage(@PathVariable UUID uuid) {
        UploadedFile file = fileService.findByUuid(uuid);
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

    @GetMapping("/open/{uuid}")
    public ResponseEntity<Resource> openFile(@PathVariable UUID uuid) throws MalformedURLException {
        UploadedFile file = fileService.findByUuid(uuid);
        if (file == null) return ResponseEntity.notFound().build();

        Resource resource = new UrlResource(Paths.get(file.getStoragePath()).toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        return fileService.deleteFile(uuid) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/rename/{uuid}")
    public ResponseEntity<UploadedFile> rename(@PathVariable UUID uuid, @RequestParam String newName) {
        UploadedFile updatedFile = fileService.renameFile(uuid, newName);
        return ResponseEntity.ok(updatedFile);
    }
}
