package com.example.springbootfilestorage.dto;

import com.example.springbootfilestorage.dao.Filetype;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadedFileDTO {
    Long id;
    Long folderId;
    String originalFilename;
    Long size;
    Filetype filetype;
}
