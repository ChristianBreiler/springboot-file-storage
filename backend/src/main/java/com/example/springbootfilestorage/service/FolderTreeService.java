package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dto.FolderNode;
import com.example.springbootfilestorage.dto.foldertree.FolderNodeDTO;
import com.example.springbootfilestorage.dto.mappers.FolderNodeDTOMapper;
import com.example.springbootfilestorage.repository.FolderRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderTreeService {

    private final FolderRepository folderRepository;
    private final FolderNodeDTOMapper folderNodeDTOMapper;
    private final UserContext userContext;

    public FolderTreeService(FolderRepository folderRepository, FolderNodeDTOMapper folderNodeDTOMapper,
                             UserContext userContext) {
        this.folderRepository = folderRepository;
        this.folderNodeDTOMapper = folderNodeDTOMapper;
        this.userContext = userContext;
    }

    public FolderNodeDTO buildTree() {
        List<Folder> rootFolders = folderRepository.findAllFoldersWithNoParents(userContext.getAuthenticatedUser());
        List<FolderNode> nodes = rootFolders
                .stream()
                .map(this::buildNode)
                .toList();
        FolderNode rootNode = new FolderNode("Home");
        rootNode.setSubfolders(nodes);
        return folderNodeDTOMapper.apply(rootNode);
    }

    private FolderNode buildNode(Folder folder) {
        FolderNode node = new FolderNode(folder.getName());
        node.setUuid(folder.getUuid());
        for (Folder sub : folder.getSubfolders()) {
            node.getSubfolders().add(buildNode(sub));
        }
        return node;
    }
}

