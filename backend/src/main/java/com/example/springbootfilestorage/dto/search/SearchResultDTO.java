package com.example.springbootfilestorage.dto.search;

import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;

import java.util.List;

public record SearchResultDTO(
        List<FolderDTO> folders,
        List<UploadedFileDTO> files
) {
}
