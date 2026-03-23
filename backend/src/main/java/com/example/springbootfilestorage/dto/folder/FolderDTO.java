package com.example.springbootfilestorage.dto.folder;

import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;

import java.util.List;

public record FolderDTO(
        Long id,
        String name,
        Long ownerId,
        List<ParentFolderDTO> parentFolders,
        List<FolderSummaryDTO> folders,
        List<UploadedFileDTO> files
) {
}
