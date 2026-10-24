package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.metadata.MetaDataDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MetaDataDTOPMapper implements Function<UploadedFile, MetaDataDTO> {

    @Override
    public MetaDataDTO apply(UploadedFile file) {
        return new MetaDataDTO(
                file.getOriginalFilename(),
                file.getSize(),
                file.getCreatedAt(),
                file.getFiletype()
        );
    }
}
