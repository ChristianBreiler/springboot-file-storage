package com.example.springbootfilestorage.dao;

import com.example.springbootfilestorage.security.dao.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "folders")
@NoArgsConstructor
public class Folder extends BaseDAO {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Folder parent;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> subfolders = new ArrayList<>();

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UploadedFile> files = new ArrayList<>();

    public List<Folder> allParents() {
        List<Folder> parents = new ArrayList<>();
        Folder currentFolder = this;
        int MAX_DEPTH = 10;
        int depth = 0;
        while (currentFolder.getParent() != null && depth++ < MAX_DEPTH) {
            parents.add(currentFolder.getParent());
            currentFolder = currentFolder.getParent();
        }
        return parents;
    }
}
