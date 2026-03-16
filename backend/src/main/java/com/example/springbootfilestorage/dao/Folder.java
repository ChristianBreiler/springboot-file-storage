package com.example.springbootfilestorage.dao;

import com.example.springbootfilestorage.security.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
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
    @JsonManagedReference
    private List<Folder> subfolders = new ArrayList<>();

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UploadedFile> files = new ArrayList<>();

    public List<Folder> allParents() {
        List<Folder> parents = new ArrayList<>();
        Folder currentFolder = this;
        while (currentFolder.getParent() != null) {
            parents.add(currentFolder.getParent());
            currentFolder = currentFolder.getParent();
        }
        return parents;
    }
}
