package com.example.springbootfilestorage.repository;

import com.example.springbootfilestorage.dao.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("SELECT f FROM Folder f WHERE f.parent IS NULL")
    List<Folder> findAllFoldersWithNoParents();

    @Query("SELECT f FROM Folder f WHERE f.parent IS NULL AND f.name LIKE %?1%")
    List<Folder> findFoldersByNameOnHomePage(String name);

    @Query("SELECT f FROM Folder f WHERE f.parent.id = ?1 AND f.name LIKE %?2%")
    List<Folder> searchFoldersByName(Long parentFolderId, String name);
}
