package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.Filetype;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.metadata.MetaDataDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MetaDataDTOPMapperTest {

    private final MetaDataDTOPMapper mapper = new MetaDataDTOPMapper();

    @Test
    void apply_mapsAllRelevantFields() {
        UploadedFile file = new UploadedFile();
        file.setOriginalFilename("report.pdf");
        file.setSize(2048L);
        file.setCreatedAt(LocalDate.of(2024, 5, 1));
        file.setFiletype(Filetype.PDF);

        MetaDataDTO result = mapper.apply(file);

        assertThat(result).isEqualTo(new MetaDataDTO("report.pdf", 2048L, LocalDate.of(2024, 5, 1), Filetype.PDF));
    }
}
