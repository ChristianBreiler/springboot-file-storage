package com.example.springbootfilestorage.dto.folder;

import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderDTO {
    Long id;
    String name;
    Long ownerId;
    List<ParentFolderDTO> parentFolders;
    List<FolderSummaryDTO> folders;
    List<UploadedFileDTO> files;
}
