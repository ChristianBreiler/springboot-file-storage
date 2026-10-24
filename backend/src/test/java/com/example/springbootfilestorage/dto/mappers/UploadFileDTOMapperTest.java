package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.Filetype;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UploadFileDTOMapperTest {

    private final UploadFileDTOMapper mapper = new UploadFileDTOMapper();

    @Test
    void apply_returnsNull_whenFileIsNull() {
        assertThat(mapper.apply(null)).isNull();
    }

    @Test
    void apply_mapsFolderUuid_whenFolderPresent() {
        Folder folder = new Folder();
        folder.setUuid(UUID.randomUUID());

        UploadedFile file = new UploadedFile();
        file.setUuid(UUID.randomUUID());
        file.setFolder(folder);
        file.setOriginalFilename("image.png");
        file.setSize(1024L);
        file.setFiletype(Filetype.PNG);
        file.setDeleted(false);

        UploadedFileDTO result = mapper.apply(file);

        assertThat(result).isEqualTo(new UploadedFileDTO(file.getUuid(), folder.getUuid(), "image.png", 1024L,
                Filetype.PNG, false));
    }

    @Test
    void apply_nullFolderUuid_whenFolderAbsent() {
        UploadedFile file = new UploadedFile();
        file.setUuid(UUID.randomUUID());
        file.setFolder(null);
        file.setOriginalFilename("audio.mp3");
        file.setSize(512L);
        file.setFiletype(Filetype.MP3);
        file.setDeleted(true);

        UploadedFileDTO result = mapper.apply(file);

        assertThat(result.folderUuid()).isNull();
        assertThat(result.isDeleted()).isTrue();
    }
}
