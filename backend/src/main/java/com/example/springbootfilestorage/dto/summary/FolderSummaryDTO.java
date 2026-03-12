package com.example.springbootfilestorage.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A Data Transfer Object (DTO) representing a summary of a folder.
 * This class provides the basic details of a folder such as its identifier and name for display purposes.
 */
@Data
@AllArgsConstructor
public class FolderSummaryDTO {
    Long id;
    String name;
}
