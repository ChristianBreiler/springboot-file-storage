package com.example.springbootfilestorage.dto.foldertree;

import java.util.List;
import java.util.UUID;

public record FolderNodeDTO(
        UUID uuid,
        String name,
        List<FolderNodeDTO> children
) {
}
