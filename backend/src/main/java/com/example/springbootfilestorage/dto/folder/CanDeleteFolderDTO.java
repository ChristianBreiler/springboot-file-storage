package com.example.springbootfilestorage.dto.folder;

// At the current state of the application, users can only delete empty folders,
// and the other elements are not recursively deleted (might be implemented in the future)
public record CanDeleteFolderDTO(
        // Checks if there are any files or folders in the folder
        boolean empty
) {
}
