package com.example.springbootfilestorage.dto;

import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FolderDTO {
    Long id;
    String name;
    Long ownerId;
    List<ParentFolderDTO> parentFolders;
    List<FolderSummaryDTO> folders;
    List<UploadedFileDTO> files;
}
