package com.example.springbootfilestorage.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileNode {
    private String name;

    public FileNode(String name) {
        this.name = name;
    }
}
