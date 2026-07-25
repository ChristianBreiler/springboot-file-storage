package com.example.springbootfilestorage.dto.foldertree;

import java.util.List;

public record FolderTreeDTO(
        List<FolderNodeDTO> folders
) {
}