package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dto.foldertree.FolderNodeDTO;
import com.example.springbootfilestorage.dto.mappers.FolderNodeDTOMapper;
import com.example.springbootfilestorage.repository.FolderRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FolderTreeServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private UserContext userContext;

    private FolderTreeService folderTreeService;
    private User user;

    @BeforeEach
    void setUp() {
        folderTreeService = new FolderTreeService(folderRepository, new FolderNodeDTOMapper(), userContext);

        user = new User();
        user.setUuid(UUID.randomUUID());
        when(userContext.getAuthenticatedUser()).thenReturn(user);
    }

    @Test
    void buildTree_noFolders_returnsEmptyHomeNode() {
        when(folderRepository.findAllFoldersWithNoParents(user)).thenReturn(List.of());

        FolderNodeDTO result = folderTreeService.buildTree();

        assertThat(result.name()).isEqualTo("Home");
        assertThat(result.uuid()).isNull();
        assertThat(result.children()).isEmpty();
    }

    @Test
    void buildTree_nestsRootFoldersAndSubfoldersUnderHome() {
        Folder documents = folder("Documents");
        Folder invoices = folder("Invoices");
        Folder year2024 = folder("2024");
        invoices.getSubfolders().add(year2024);
        documents.getSubfolders().add(invoices);
        Folder pictures = folder("Pictures");

        when(folderRepository.findAllFoldersWithNoParents(user)).thenReturn(List.of(documents, pictures));

        FolderNodeDTO result = folderTreeService.buildTree();

        assertThat(result.name()).isEqualTo("Home");
        assertThat(result.children()).extracting(FolderNodeDTO::name).containsExactly("Documents", "Pictures");

        FolderNodeDTO documentsNode = result.children().get(0);
        assertThat(documentsNode.uuid()).isEqualTo(documents.getUuid());
        assertThat(documentsNode.children()).extracting(FolderNodeDTO::name).containsExactly("Invoices");

        FolderNodeDTO invoicesNode = documentsNode.children().get(0);
        assertThat(invoicesNode.uuid()).isEqualTo(invoices.getUuid());
        assertThat(invoicesNode.children()).extracting(FolderNodeDTO::uuid).containsExactly(year2024.getUuid());

        assertThat(result.children().get(1).children()).isEmpty();
    }

    private Folder folder(String name) {
        Folder folder = new Folder();
        folder.setUuid(UUID.randomUUID());
        folder.setName(name);
        return folder;
    }
}
