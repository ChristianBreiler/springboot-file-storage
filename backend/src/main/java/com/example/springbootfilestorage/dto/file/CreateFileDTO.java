package com.example.springbootfilestorage.dto.file;

import org.springframework.web.multipart.MultipartFile;

public record CreateFileDTO(
        MultipartFile file
) {
}
