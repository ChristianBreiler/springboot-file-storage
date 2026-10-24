package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.Filetype;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class FolderDTOMapperTest {

    @Mock
    private UserContext userContext;

    private FolderDTOMapper mapper;
    private UUID currentUserUuid;

    @BeforeEach
    void setUp() {
        mapper = new FolderDTOMapper(userContext, new UploadFileDTOMapper());

        User currentUser = new User();
        currentUserUuid = UUID.randomUUID();
        currentUser.setUuid(currentUserUuid);
        lenient().when(userContext.getAuthenticatedUser()).thenReturn(currentUser);
    }

    @Test
    void apply_returnsNull_whenFolderIsNull() {
        assertThat(mapper.apply(null)).isNull();
    }

    @Test
    void apply_mapsBreadcrumbsInRootToLeafOrder() {
        Folder grandparent = new Folder();
        grandparent.setUuid(UUID.randomUUID());
        grandparent.setName("Grandparent");

        Folder parent = new Folder();
        parent.setUuid(UUID.randomUUID());
        parent.setName("Parent");
        parent.setParent(grandparent);

        Folder folder = new Folder();
        folder.setUuid(UUID.randomUUID());
        folder.setName("Child");
        folder.setParent(parent);

        FolderDTO result = mapper.apply(folder);

        assertThat(result.parentFolders()).extracting("name")
                .containsExactly("Grandparent", "Parent");
        assertThat(result.ownerUuid()).isEqualTo(currentUserUuid);
    }

    @Test
    void apply_filtersOutDeletedFiles() {
        Folder folder = new Folder();
        folder.setUuid(UUID.randomUUID());
        folder.setName("Documents");

        UploadedFile visible = new UploadedFile();
        visible.setUuid(UUID.randomUUID());
        visible.setOriginalFilename("visible.pdf");
        visible.setFiletype(Filetype.PDF);
        visible.setDeleted(false);

        UploadedFile deleted = new UploadedFile();
        deleted.setUuid(UUID.randomUUID());
        deleted.setOriginalFilename("deleted.pdf");
        deleted.setFiletype(Filetype.PDF);
        deleted.setDeleted(true);

        folder.setFiles(List.of(visible, deleted));

        FolderDTO result = mapper.apply(folder);

        assertThat(result.files()).extracting("originalFilename").containsExactly("visible.pdf");
    }

    @Test
    void apply_emptySubfoldersAndFiles_whenNull() {
        Folder folder = new Folder();
        folder.setUuid(UUID.randomUUID());
        folder.setName("Empty");
        folder.setSubfolders(null);
        folder.setFiles(null);

        FolderDTO result = mapper.apply(folder);

        assertThat(result.folders()).isEmpty();
        assertThat(result.files()).isEmpty();
    }
}
