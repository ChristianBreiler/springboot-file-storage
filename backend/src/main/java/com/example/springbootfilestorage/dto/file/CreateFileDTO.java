package com.example.springbootfilestorage.dto.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateFileDTO(
        List<MultipartFile> files,
        String folderName
) {
}
