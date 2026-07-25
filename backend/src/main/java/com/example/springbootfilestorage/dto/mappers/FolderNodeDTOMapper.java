package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.FolderNode;
import com.example.springbootfilestorage.dto.foldertree.FolderNodeDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FolderNodeDTOMapper implements Function<FolderNode, FolderNodeDTO> {
    @Override
    public FolderNodeDTO apply(FolderNode folderNode) {
        return new FolderNodeDTO(
                folderNode.getUuid(),
                folderNode.getName(),
                folderNode.getSubfolders().stream().map(this).toList()
        );
    }
}
