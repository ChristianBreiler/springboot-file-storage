package com.example.springbootfilestorage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FolderNode {

    private Long id;
    private String name;
    private boolean directory = true;
    private List<FolderNode> subfolders = new ArrayList<>();
    private List<FileNode> files = new ArrayList<>();

    public FolderNode(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
