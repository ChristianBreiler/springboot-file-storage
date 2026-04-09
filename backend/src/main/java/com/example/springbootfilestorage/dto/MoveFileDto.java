package com.example.springbootfilestorage.dto;

import java.util.UUID;

// Used tp drag a file into a folder
public record MoveFileDto(
        UUID fileUuid,
        UUID folderUuid
) {
}
