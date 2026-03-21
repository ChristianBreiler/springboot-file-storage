package com.example.springbootfilestorage.dto;

import com.example.springbootfilestorage.dao.Filetype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFileDTO {
    Long id;
    Long folderId;
    String originalFilename;
    Long size;
    Filetype filetype;
}
