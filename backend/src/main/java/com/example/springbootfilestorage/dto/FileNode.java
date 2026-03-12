package com.example.springbootfilestorage.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileNode {
    private Long id;
    private String name;

    public FileNode(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
