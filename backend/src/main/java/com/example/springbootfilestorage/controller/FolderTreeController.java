package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.FolderNode;
import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.service.FolderService;
import com.example.springbootfilestorage.service.FolderTreeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foldertree")
public class FolderTreeController {

    private final FolderTreeService folderTreeService;
    private final FolderService folderService;

    public FolderTreeController(FolderTreeService folderTreeService,
                                FolderService folderService) {
        this.folderTreeService = folderTreeService;
        this.folderService = folderService;
    }

    @RequestMapping("/{id}")
    public String folderInspect(@PathVariable Long id, Model model) {
        Folder folder = folderService.findById(id);
        model.addAttribute("folder_name", folder.getName());
        FolderNode tree = folderTreeService.buildTree(id);
        model.addAttribute("tree", tree);
        return "folder_tree";
    }
}
