package com.example.springbootfilestorage.dto.file;

import com.example.springbootfilestorage.dao.Filetype;

public record UploadedFileDTO(
        Long id,
        Long folderId,
        String originalFilename,
        Long size,
        Filetype filetype
) {
}
