package com.example.springbootfilestorage.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Used in the frontend for the clickable folders
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderSummaryDTO {
    Long id;
    String name;
}
