package com.example.springbootfilestorage.dto.metadata;

import com.example.springbootfilestorage.dao.Filetype;

import java.time.LocalDate;

public record MetaDataDTO(
        String originalFileName,
        Long size,
        LocalDate createdAt,
        Filetype filetype
) {
}
