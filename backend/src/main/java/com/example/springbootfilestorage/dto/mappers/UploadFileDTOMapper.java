package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UploadFileDTOMapper implements Function<UploadedFile, UploadedFileDTO> {
    @Override
    public UploadedFileDTO apply(UploadedFile file) {
        if (file == null) return null;
        return new UploadedFileDTO(
                file.getUuid(),
                file.getFolder() != null ? file.getFolder().getUuid() : null,
                file.getOriginalFilename(),
                file.getSize(),
                file.getFiletype(),
                file.isDeleted()
        );
    }
}
