package com.example.springbootfilestorage.dto;

import java.util.UUID;

public record MoveFolderDto(
        UUID folderUuid,
        UUID targetFolderUuid
) {
}
