package com.example.springbootfilestorage.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

// Used in StorageDetailDTO for the top 5 largest files

@Data
@AllArgsConstructor
public class FileSizeDTO {
    String filename;
    double size;
}
