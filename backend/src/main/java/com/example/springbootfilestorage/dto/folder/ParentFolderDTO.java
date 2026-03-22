package com.example.springbootfilestorage.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Used for Breadcrumbs in the frontend

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentFolderDTO {
    Long id;
    String name;
}
