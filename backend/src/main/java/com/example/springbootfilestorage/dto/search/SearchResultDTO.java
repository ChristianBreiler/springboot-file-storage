package com.example.springbootfilestorage.dto.search;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;

import java.util.List;

public record SearchResultDTO(
        List<Folder> folders,
        List<UploadedFile> files
) {
}
