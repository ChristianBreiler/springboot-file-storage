package com.example.springbootfilestorage.dto.folder;

import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;

import java.util.List;
import java.util.UUID;

public record FolderDTO(
        UUID uuid,
        String name,
        UUID ownerUuid,
        List<ParentFolderDTO> parentFolders,
        List<FolderSummaryDTO> folders,
        List<UploadedFileDTO> files
) {
}
