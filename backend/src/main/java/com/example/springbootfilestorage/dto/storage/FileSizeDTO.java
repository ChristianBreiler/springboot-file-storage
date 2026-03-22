package com.example.springbootfilestorage.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Used in StorageDetailDTO for the top 5 largest files

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSizeDTO {
    String filename;
    double size;
}
