package com.example.springbootfilestorage.dto.folder;

// Used for Breadcrumbs in the frontend

import java.util.UUID;

public record ParentFolderDTO(
        UUID uuid,
        String name
) {
}
