package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.FolderNode;
import com.example.springbootfilestorage.dto.foldertree.FolderNodeDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FolderNodeDTOMapperTest {

    private final FolderNodeDTOMapper mapper = new FolderNodeDTOMapper();

    @Test
    void apply_mapsLeafNode() {
        UUID uuid = UUID.randomUUID();
        FolderNode node = new FolderNode("Documents");
        node.setUuid(uuid);

        FolderNodeDTO result = mapper.apply(node);

        assertThat(result).isEqualTo(new FolderNodeDTO(uuid, "Documents", List.of()));
    }

    @Test
    void apply_mapsSubfoldersRecursively() {
        FolderNode grandchild = new FolderNode("Grandchild");
        grandchild.setUuid(UUID.randomUUID());

        FolderNode child = new FolderNode("Child");
        child.setUuid(UUID.randomUUID());
        child.getSubfolders().add(grandchild);

        FolderNode root = new FolderNode("Root");
        root.getSubfolders().add(child);

        FolderNodeDTO result = mapper.apply(root);

        assertThat(result.uuid()).isNull();
        assertThat(result.children()).extracting(FolderNodeDTO::name).containsExactly("Child");
        assertThat(result.children().get(0).uuid()).isEqualTo(child.getUuid());
        assertThat(result.children().get(0).children()).extracting(FolderNodeDTO::name)
                .containsExactly("Grandchild");
    }
}
