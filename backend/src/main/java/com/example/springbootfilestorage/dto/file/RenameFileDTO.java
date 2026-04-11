package com.example.springbootfilestorage.dto.file;

public record RenameFileDTO(
        String newFileName,
        String fileType
) {
}
