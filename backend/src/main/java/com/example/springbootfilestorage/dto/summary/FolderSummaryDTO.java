package com.example.springbootfilestorage.dto.summary;

import java.util.UUID;

// Used in the frontend for the clickable folders
public record FolderSummaryDTO(
        UUID uuid,
        String name
) {
}
