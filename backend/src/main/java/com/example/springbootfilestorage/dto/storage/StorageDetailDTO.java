package com.example.springbootfilestorage.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class StorageDetailDTO {
    double usedSpace;
    double totalSpace;
    int numberOfFiles;
    int numberOfFolders;
    List<FileSizeDTO> fiveLargestFiles;
}
