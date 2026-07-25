package com.example.springbootfilestorage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FolderNode {

    private UUID uuid;
    private String name;
    private boolean directory = true;
    private List<FolderNode> subfolders = new ArrayList<>();

    public FolderNode(String name) {
        this.name = name;
    }
}
