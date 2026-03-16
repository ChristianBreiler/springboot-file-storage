package com.example.springbootfilestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Used for Breadcrumbs in the frontend

@Data
@AllArgsConstructor
public class ParentFolderDTO {
    Long id;
    String name;
}
