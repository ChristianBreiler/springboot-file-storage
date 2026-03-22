package com.example.springbootfilestorage.dto.search;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResultDTO {
    private List<Folder> folders;
    private List<UploadedFile> files;
}
