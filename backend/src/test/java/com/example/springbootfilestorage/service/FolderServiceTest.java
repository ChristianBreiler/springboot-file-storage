package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.folder.CanDeleteFolderDTO;
import com.example.springbootfilestorage.dto.folder.CreateFolderDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.folder.RenameFolderDTO;
import com.example.springbootfilestorage.dto.mappers.FolderDTOMapper;
import com.example.springbootfilestorage.dto.mappers.UploadFileDTOMapper;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserContext userContext;

    private FolderService folderService;
    private User user;

    @BeforeEach
    void setUp() {
        FolderDTOMapper folderDTOMapper = new FolderDTOMapper(userContext, new UploadFileDTOMapper());
        folderService = new FolderService(folderRepository, fileRepository, userContext, folderDTOMapper);

        user = new User();
        user.setUuid(UUID.randomUUID());
    }

    @Test
    void saveFolder_root_savesWithoutParent() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.save(any(Folder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FolderDTO result = folderService.saveFolder(new CreateFolderDTO("Documents"), null);

        assertThat(result.name()).isEqualTo("Documents");
        assertThat(result.parentFolders()).isEmpty();
        verify(folderRepository).save(argThat(f -> f.getParent() == null && f.getOwner() == user));
    }

    @Test
    void saveFolder_withParent_setsParentAndSaves() {
        UUID parentUuid = UUID.randomUUID();
        Folder parent = new Folder();
        parent.setUuid(parentUuid);
        parent.setName("Parent");
        parent.setSubfolders(new ArrayList<>());

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findByUuid(parentUuid)).thenReturn(Optional.of(parent));
        when(folderRepository.save(any(Folder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FolderDTO result = folderService.saveFolder(new CreateFolderDTO("Sub"), parentUuid);

        assertThat(result.name()).isEqualTo("Sub");
        verify(folderRepository).save(argThat(f -> f.getParent() == parent));
    }

    @Test
    void saveFolder_parentNotFound_throws() {
        UUID parentUuid = UUID.randomUUID();
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findByUuid(parentUuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> folderService.saveFolder(new CreateFolderDTO("Sub"), parentUuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Parent folder not found");
    }

    @Test
    void saveFolder_maxSubfoldersReached_throws() {
        UUID parentUuid = UUID.randomUUID();
        Folder parent = new Folder();
        parent.setUuid(parentUuid);
        List<Folder> fiveSubfolders = List.of(new Folder(), new Folder(), new Folder(), new Folder(), new Folder());
        parent.setSubfolders(fiveSubfolders);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findByUuid(parentUuid)).thenReturn(Optional.of(parent));

        assertThatThrownBy(() -> folderService.saveFolder(new CreateFolderDTO("Sub"), parentUuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maximum number of subfolders reached");
        verify(folderRepository, never()).save(any());
    }

    @Test
    void renameFolder_updatesNameAndSaves() {
        UUID uuid = UUID.randomUUID();
        Folder folder = new Folder();
        folder.setUuid(uuid);
        folder.setName("Old");
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.of(folder));
        when(userContext.getAuthenticatedUser()).thenReturn(user);

        FolderDTO result = folderService.renameFolder(uuid, new RenameFolderDTO("New"));

        assertThat(folder.getName()).isEqualTo("New");
        assertThat(result.name()).isEqualTo("New");
        verify(folderRepository).save(folder);
    }

    @Test
    void renameFolder_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> folderService.renameFolder(uuid, new RenameFolderDTO("New")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Folder not found");
    }

    @Test
    void deleteFolder_found_deletesIt() {
        UUID uuid = UUID.randomUUID();
        Folder folder = new Folder();
        folder.setUuid(uuid);
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.of(folder));

        folderService.deleteFolder(uuid);

        verify(folderRepository).delete(folder);
    }

    @Test
    void deleteFolder_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> folderService.deleteFolder(uuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
        verify(folderRepository, never()).delete(any());
    }

    @Test
    void searchFoldersByName_mapsMatches() {
        Folder folder = new Folder();
        folder.setUuid(UUID.randomUUID());
        folder.setName("Invoices");
        when(folderRepository.searchFoldersByName("inv", user)).thenReturn(List.of(folder));
        when(userContext.getAuthenticatedUser()).thenReturn(user);

        List<FolderDTO> result = folderService.searchFoldersByName("inv");

        assertThat(result).extracting("name").containsExactly("Invoices");
    }

    @Test
    void findHomeDTO_buildsFolderFromRootLevelData() {
        Folder subfolder = new Folder();
        subfolder.setUuid(UUID.randomUUID());
        subfolder.setName("Sub");
        UploadedFile file = new UploadedFile();
        file.setUuid(UUID.randomUUID());
        file.setOriginalFilename("a.pdf");

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findAllFoldersWithNoParents(user)).thenReturn(List.of(subfolder));
        when(fileRepository.findAllFilesWithNoFolder(user)).thenReturn(List.of(file));

        FolderDTO result = folderService.findHomeDTO();

        assertThat(result.folders()).extracting("name").containsExactly("Sub");
        assertThat(result.files()).extracting("originalFilename").containsExactly("a.pdf");
        assertThat(result.parentFolders()).isEmpty();
    }

    @Test
    void canFolderBeDeleted_emptyFolder_returnsTrue() {
        UUID uuid = UUID.randomUUID();
        Folder folder = new Folder();
        folder.setUuid(uuid);
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.of(folder));

        CanDeleteFolderDTO result = folderService.canFolderBeDeleted(uuid);

        assertThat(result.empty()).isTrue();
    }

    @Test
    void canFolderBeDeleted_nonEmptyFolder_returnsFalse() {
        UUID uuid = UUID.randomUUID();
        Folder folder = new Folder();
        folder.setUuid(uuid);
        folder.setSubfolders(List.of(new Folder()));
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.of(folder));

        CanDeleteFolderDTO result = folderService.canFolderBeDeleted(uuid);

        assertThat(result.empty()).isFalse();
    }

    @Test
    void findByUuid_found_returnsFolder() {
        UUID uuid = UUID.randomUUID();
        Folder folder = new Folder();
        folder.setUuid(uuid);
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.of(folder));

        assertThat(folderService.findByUuid(uuid)).isSameAs(folder);
    }

    @Test
    void findByUuid_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> folderService.findByUuid(uuid)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void findByDTOUuid_found_returnsMappedFolder() {
        UUID uuid = UUID.randomUUID();
        Folder folder = new Folder();
        folder.setUuid(uuid);
        folder.setName("Documents");
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.of(folder));

        FolderDTO result = folderService.findByDTOUuid(uuid);

        assertThat(result.uuid()).isEqualTo(uuid);
        assertThat(result.name()).isEqualTo("Documents");
    }

    @Test
    void findByDTOUuid_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(folderRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> folderService.findByDTOUuid(uuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Folder not found");
    }

    @Test
    void getAllFolders_mapsFoldersOfCurrentUser() {
        Folder folder = new Folder();
        folder.setUuid(UUID.randomUUID());
        folder.setName("Documents");
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findAllFolders(user)).thenReturn(Optional.of(folder));

        List<FolderDTO> result = folderService.getAllFolders();

        assertThat(result).extracting(FolderDTO::name).containsExactly("Documents");
    }

    @Test
    void getAllFolders_noFolders_returnsEmptyList() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findAllFolders(user)).thenReturn(Optional.empty());

        assertThat(folderService.getAllFolders()).isEmpty();
    }
}
