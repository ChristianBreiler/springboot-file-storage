package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Filetype;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.MoveFileDto;
import com.example.springbootfilestorage.dto.file.CreateFileDTO;
import com.example.springbootfilestorage.dto.file.RenameFileDTO;
import com.example.springbootfilestorage.dto.file.UploadedFileDTO;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.mappers.FolderDTOMapper;
import com.example.springbootfilestorage.dto.mappers.MetaDataDTOPMapper;
import com.example.springbootfilestorage.dto.mappers.UploadFileDTOMapper;
import com.example.springbootfilestorage.dto.metadata.MetaDataDTO;
import com.example.springbootfilestorage.repository.FileRepository;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.scripts.system.StoragePathBean;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private FolderService folderService;
    @Mock
    private FolderRepository folderRepository;
    @Mock
    private StoragePathBean storagePathBean;
    @Mock
    private UserContext userContext;

    @TempDir
    Path tempDir;

    private FileService fileService;
    private User user;

    @BeforeEach
    void setUp() {
        when(storagePathBean.getStorageFolderPath()).thenReturn(tempDir);

        UploadFileDTOMapper uploadFileDTOMapper = new UploadFileDTOMapper();
        FolderDTOMapper folderDTOMapper = new FolderDTOMapper(userContext, uploadFileDTOMapper);
        MetaDataDTOPMapper metaDataDTOPMapper = new MetaDataDTOPMapper();

        fileService = new FileService(fileRepository, folderService, folderRepository, storagePathBean, userContext,
                uploadFileDTOMapper, folderDTOMapper, metaDataDTOPMapper);

        user = new User();
        user.setUuid(UUID.randomUUID());
    }

    // ---- saveFile ----

    @Test
    void saveFile_noFolderName_storesFileOnDiskAndReturnsDto() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(fileRepository.findByNameAndFolderUuid("test.pdf", null)).thenReturn(null);
        when(fileRepository.save(any(UploadedFile.class))).thenAnswer(inv -> inv.getArgument(0));

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf", "application/pdf",
                "hello".getBytes());
        CreateFileDTO dto = new CreateFileDTO(List.of(multipartFile), null);

        List<UploadedFileDTO> result = fileService.saveFile(dto, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).originalFilename()).isEqualTo("test.pdf");
        assertThat(result.get(0).filetype()).isEqualTo(Filetype.PDF);

        ArgumentCaptor<UploadedFile> captor = ArgumentCaptor.forClass(UploadedFile.class);
        verify(fileRepository).save(captor.capture());
        Path storedPath = Path.of(captor.getValue().getStoragePath());
        assertThat(Files.exists(storedPath)).isTrue();
        assertThat(storedPath.getParent()).isEqualTo(tempDir);
    }

    @Test
    void saveFile_withFolderName_createsRootFolderAndAttachesFile() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.save(any(Folder.class))).thenAnswer(inv -> inv.getArgument(0));
        when(fileRepository.findByNameAndFolderUuid(eq("test.pdf"), isNull())).thenReturn(null);
        when(fileRepository.save(any(UploadedFile.class))).thenAnswer(inv -> inv.getArgument(0));

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf", "application/pdf",
                "hello".getBytes());
        CreateFileDTO dto = new CreateFileDTO(List.of(multipartFile), "  NewFolder  ");

        fileService.saveFile(dto, null);

        verify(folderRepository).save(argThat(f ->
                f.getName().equals("NewFolder") && f.getOwner() == user && f.getParent() == null));
        ArgumentCaptor<UploadedFile> captor = ArgumentCaptor.forClass(UploadedFile.class);
        verify(fileRepository).save(captor.capture());
        assertThat(captor.getValue().getFolder()).isNotNull();
        assertThat(captor.getValue().getFolder().getName()).isEqualTo("NewFolder");
    }

    @Test
    void saveFile_duplicateFileName_throws() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(fileRepository.findByNameAndFolderUuid(eq("dup.pdf"), isNull()))
                .thenReturn(new UploadedFile());

        MockMultipartFile multipartFile = new MockMultipartFile("file", "dup.pdf", "application/pdf",
                "hello".getBytes());
        CreateFileDTO dto = new CreateFileDTO(List.of(multipartFile), null);

        assertThatThrownBy(() -> fileService.saveFile(dto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void saveFile_unsupportedFileType_throws() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(fileRepository.findByNameAndFolderUuid(eq("virus.exe"), isNull())).thenReturn(null);

        MockMultipartFile multipartFile = new MockMultipartFile("file", "virus.exe", "application/octet-stream",
                "hello".getBytes());
        CreateFileDTO dto = new CreateFileDTO(List.of(multipartFile), null);

        assertThatThrownBy(() -> fileService.saveFile(dto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported or invalid file type");
    }

    @Test
    void saveFile_emptyFile_throws() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);

        MockMultipartFile multipartFile = new MockMultipartFile("file", "empty.pdf", "application/pdf",
                new byte[0]);
        CreateFileDTO dto = new CreateFileDTO(List.of(multipartFile), null);

        assertThatThrownBy(() -> fileService.saveFile(dto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("missing or file is empty");
    }

    // ---- findAllDeletedFiles / findAll / searchFilesByName ----

    @Test
    void findAllDeletedFiles_mapsRepositoryResults() {
        UploadedFile file = sampleFile("deleted.pdf");
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(fileRepository.findAllDeletedFiles(user)).thenReturn(List.of(file));

        assertThat(fileService.findAllDeletedFiles()).extracting("originalFilename")
                .containsExactly("deleted.pdf");
    }

    @Test
    void findAll_mapsRepositoryResults() {
        UploadedFile file = sampleFile("a.pdf");
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(fileRepository.findAllFiles(user)).thenReturn(List.of(file));

        assertThat(fileService.findAll()).extracting("originalFilename").containsExactly("a.pdf");
    }

    @Test
    void searchFilesByName_mapsMatches() {
        UploadedFile file = sampleFile("invoice.pdf");
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(fileRepository.findByName("inv", user)).thenReturn(List.of(file));

        assertThat(fileService.searchFilesByName("inv")).extracting("originalFilename")
                .containsExactly("invoice.pdf");
    }

    // ---- deleteFile ----

    @Test
    void deleteFile_found_marksDeletedAndReturnsTrue() {
        UUID uuid = UUID.randomUUID();
        UploadedFile file = sampleFile("a.pdf");
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.of(file));

        boolean result = fileService.deleteFile(uuid);

        assertThat(result).isTrue();
        assertThat(file.isDeleted()).isTrue();
        assertThat(file.getDeletedAt()).isNotNull();
        verify(fileRepository).save(file);
    }

    @Test
    void deleteFile_notFound_returnsFalse() {
        UUID uuid = UUID.randomUUID();
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThat(fileService.deleteFile(uuid)).isFalse();
        verify(fileRepository, never()).save(any());
    }

    @Test
    void deleteFile_saveFails_returnsFalse() {
        UUID uuid = UUID.randomUUID();
        UploadedFile file = sampleFile("a.pdf");
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.of(file));
        when(fileRepository.save(file)).thenThrow(new RuntimeException("db error"));

        assertThat(fileService.deleteFile(uuid)).isFalse();
    }

    // ---- restoreFile ----

    @Test
    void restoreFile_found_clearsDeletedFlag() {
        UUID uuid = UUID.randomUUID();
        UploadedFile file = sampleFile("a.pdf");
        file.setDeleted(true);
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.of(file));

        UploadedFileDTO result = fileService.restoreFile(uuid);

        assertThat(file.isDeleted()).isFalse();
        assertThat(file.getDeletedAt()).isNull();
        assertThat(result.isDeleted()).isFalse();
        verify(fileRepository).save(file);
    }

    @Test
    void restoreFile_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.restoreFile(uuid)).isInstanceOf(RuntimeException.class);
    }

    // ---- deleteFilePermanently ----

    @Test
    void deleteFilePermanently_notFound_returnsFalse() {
        UUID uuid = UUID.randomUUID();
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThat(fileService.deleteFilePermanently(uuid)).isFalse();
        verify(fileRepository, never()).delete(any());
    }

    @Test
    void deleteFilePermanently_fileOnDisk_deletesAndReturnsTrue() throws IOException {
        UUID uuid = UUID.randomUUID();
        Path onDisk = tempDir.resolve("real.pdf");
        Files.writeString(onDisk, "content");
        UploadedFile file = sampleFile("real.pdf");
        file.setStoragePath(onDisk.toString());
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.of(file));

        assertThat(fileService.deleteFilePermanently(uuid)).isTrue();
        assertThat(Files.exists(onDisk)).isFalse();
        verify(fileRepository).delete(file);
    }

    @Test
    void deleteFilePermanently_missingOnDisk_returnsFalseAndSkipsRepositoryDelete() {
        UUID uuid = UUID.randomUUID();
        UploadedFile file = sampleFile("ghost.pdf");
        file.setStoragePath(tempDir.resolve("ghost.pdf").toString());
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.of(file));

        assertThat(fileService.deleteFilePermanently(uuid)).isFalse();
        verify(fileRepository, never()).delete(any());
    }

    // ---- fileExists ----

    @Test
    void fileExists_trueWhenRepositoryReturnsMatch() {
        UUID folderUuid = UUID.randomUUID();
        when(fileRepository.findByNameAndFolderUuid("a.pdf", folderUuid)).thenReturn(sampleFile("a.pdf"));

        assertThat(fileService.fileExists("a.pdf", folderUuid)).isTrue();
    }

    @Test
    void fileExists_falseWhenNoMatch() {
        UUID folderUuid = UUID.randomUUID();
        when(fileRepository.findByNameAndFolderUuid("a.pdf", folderUuid)).thenReturn(null);

        assertThat(fileService.fileExists("a.pdf", folderUuid)).isFalse();
    }

    // ---- moveFileToFolder ----

    @Test
    void moveFileToFolder_fileNotFound_throws() {
        UUID fileUuid = UUID.randomUUID();
        when(fileRepository.findByUuid(fileUuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.moveFileToFolder(new MoveFileDto(fileUuid, UUID.randomUUID())))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("File not found");
    }

    @Test
    void moveFileToFolder_toAnotherFolder_returnsOriginalFolderDto() {
        UUID fileUuid = UUID.randomUUID();
        UUID targetFolderUuid = UUID.randomUUID();

        Folder originalFolder = new Folder();
        originalFolder.setUuid(UUID.randomUUID());
        originalFolder.setName("Original");

        Folder targetFolder = new Folder();
        targetFolder.setUuid(targetFolderUuid);
        targetFolder.setName("Target");

        UploadedFile file = sampleFile("a.pdf");
        file.setFolder(originalFolder);

        when(fileRepository.findByUuid(fileUuid)).thenReturn(Optional.of(file));
        when(folderService.findByUuid(targetFolderUuid)).thenReturn(targetFolder);
        when(userContext.getAuthenticatedUser()).thenReturn(user);

        FolderDTO result = fileService.moveFileToFolder(new MoveFileDto(fileUuid, targetFolderUuid));

        assertThat(file.getFolder()).isSameAs(targetFolder);
        assertThat(result.name()).isEqualTo("Original");
        verify(fileRepository).save(file);
    }

    @Test
    void moveFileToFolder_fileWasInHomeFolder_returnsRebuiltHomeFolderDto() {
        UUID fileUuid = UUID.randomUUID();
        UUID targetFolderUuid = UUID.randomUUID();

        Folder targetFolder = new Folder();
        targetFolder.setUuid(targetFolderUuid);
        targetFolder.setName("Target");

        UploadedFile file = sampleFile("a.pdf");
        file.setFolder(null);

        Folder rootFolder = new Folder();
        rootFolder.setUuid(UUID.randomUUID());
        rootFolder.setName("RootFolder");

        when(fileRepository.findByUuid(fileUuid)).thenReturn(Optional.of(file));
        when(folderService.findByUuid(targetFolderUuid)).thenReturn(targetFolder);
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(folderRepository.findAllFoldersWithNoParents(user)).thenReturn(List.of(rootFolder));
        when(fileRepository.findAllFilesWithNoFolder(user)).thenReturn(List.of());

        FolderDTO result = fileService.moveFileToFolder(new MoveFileDto(fileUuid, targetFolderUuid));

        assertThat(result.parentFolders()).isEmpty();
        assertThat(result.folders()).extracting("name").containsExactly("RootFolder");
    }

    // ---- renameFile ----

    @Test
    void renameFile_found_updatesFilenameAndExtension() {
        UUID uuid = UUID.randomUUID();
        UploadedFile file = sampleFile("old.pdf");
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.of(file));

        UploadedFileDTO result = fileService.renameFile(uuid, new RenameFileDTO("report", "PDF"));

        assertThat(file.getOriginalFilename()).isEqualTo("report.pdf");
        assertThat(result.originalFilename()).isEqualTo("report.pdf");
        verify(fileRepository).save(file);
    }

    @Test
    void renameFile_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.renameFile(uuid, new RenameFileDTO("report", "PDF")))
                .isInstanceOf(RuntimeException.class);
    }

    // ---- findByUuid / findDTOByUuid ----

    @Test
    void findByUuid_notFound_returnsNull() {
        UUID uuid = UUID.randomUUID();
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThat(fileService.findByUuid(uuid)).isNull();
    }

    @Test
    void findDTOByUuid_notFound_returnsNull() {
        UUID uuid = UUID.randomUUID();
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThat(fileService.findDTOByUuid(uuid)).isNull();
    }

    // ---- deleteAllDeletedFiles ----

    @Test
    void deleteAllDeletedFiles_countsOnlySuccessfulDeletions() throws IOException {
        Path existingPath = tempDir.resolve("existing.pdf");
        Files.writeString(existingPath, "content");

        UploadedFile existingOnDisk = sampleFile("existing.pdf");
        existingOnDisk.setStoragePath(existingPath.toString());

        UploadedFile missingOnDisk = sampleFile("missing.pdf");
        missingOnDisk.setStoragePath(tempDir.resolve("missing.pdf").toString());

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(fileRepository.findAllDeletedFiles(user)).thenReturn(List.of(existingOnDisk, missingOnDisk));
        when(fileRepository.findByUuid(existingOnDisk.getUuid())).thenReturn(Optional.of(existingOnDisk));
        when(fileRepository.findByUuid(missingOnDisk.getUuid())).thenReturn(Optional.of(missingOnDisk));

        int deletedCount = fileService.deleteAllDeletedFiles();

        assertThat(deletedCount).isEqualTo(1);
        verify(fileRepository).delete(existingOnDisk);
        verify(fileRepository, never()).delete(missingOnDisk);
    }

    // ---- getMetaData ----

    @Test
    void getMetaData_found_returnsMappedMetaData() {
        UUID uuid = UUID.randomUUID();
        UploadedFile file = sampleFile("a.pdf");
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.of(file));

        MetaDataDTO result = fileService.getMetaData(uuid);

        assertThat(result.originalFileName()).isEqualTo("a.pdf");
        assertThat(result.filetype()).isEqualTo(Filetype.PDF);
    }

    @Test
    void getMetaData_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(fileRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.getMetaData(uuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File not found");
    }

    // ---- saveProfilePic ----

    @Test
    void saveProfilePic_validImage_savesFileForCurrentUser() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        MockMultipartFile multipartFile = new MockMultipartFile("file", "avatar.png", "image/png",
                "img".getBytes());

        fileService.saveProfilePic(multipartFile);

        ArgumentCaptor<UploadedFile> captor = ArgumentCaptor.forClass(UploadedFile.class);
        verify(fileRepository).save(captor.capture());
        UploadedFile saved = captor.getValue();
        assertThat(saved.getOriginalFilename()).isEqualTo("avatar.png");
        assertThat(saved.getStoredName()).isNotBlank().isNotEqualTo("avatar.png");
        assertThat(saved.getSize()).isEqualTo(3L);
        assertThat(saved.getOwner()).isSameAs(user);
        assertThat(saved.getFiletype()).isEqualTo(Filetype.PNG);
    }

    @Test
    void saveProfilePic_emptyFile_doesNothing() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "avatar.png", "image/png", new byte[0]);

        fileService.saveProfilePic(multipartFile);

        verify(fileRepository, never()).save(any());
    }

    @Test
    void saveProfilePic_unsupportedFiletype_doesNotSave() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        MockMultipartFile multipartFile = new MockMultipartFile("file", "avatar.gif", "image/gif",
                "img".getBytes());

        fileService.saveProfilePic(multipartFile);

        verify(fileRepository, never()).save(any());
    }

    private UploadedFile sampleFile(String originalFilename) {
        UploadedFile file = new UploadedFile();
        file.setUuid(UUID.randomUUID());
        file.setOriginalFilename(originalFilename);
        file.setSize(123L);
        file.setFiletype(Filetype.PDF);
        file.setDeleted(false);
        return file;
    }
}
