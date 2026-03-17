package com.example.springbootfilestorage.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageDTO {
    double usedSpace;
    double totalSpace;
}
