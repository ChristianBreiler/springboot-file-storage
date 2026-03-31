package com.example.springbootfilestorage.dto.file;

import com.example.springbootfilestorage.dao.Filetype;

import java.util.UUID;

public record UploadedFileDTO(
        UUID uuid,
        UUID folderUuid,
        String originalFilename,
        Long size,
        Filetype filetype,
        boolean isDeleted
) {
}
