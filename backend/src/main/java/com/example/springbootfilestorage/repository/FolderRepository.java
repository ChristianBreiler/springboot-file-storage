package com.example.springbootfilestorage.repository;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.security.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("SELECT f FROM Folder f WHERE f.parent IS NULL AND f.deleted = false AND f.owner = ?1")
    List<Folder> findAllFoldersWithNoParents(User authenticatedUser);

    @Query("""
                SELECT f
                FROM Folder f
                WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))
                AND f.deleted = false
                AND f.owner = :user
            """)
    List<Folder> searchFoldersByName(
            @Param("name") String name,
            @Param("user") User user
    );

    @Query("SELECT COUNT(f) FROM Folder f")
    int numberOfFolders();

    @Query("SELECT f FROM Folder f WHERE f.uuid = ?1")
    Optional<Folder> findByUuid(UUID uuid);

    @Query("SELECT f FROM Folder f WHERE f.owner = ?1")
    Optional<Folder> findAllFolders(User user);

    @Query("""
                SELECT f
                FROM Folder f
                LEFT JOIN f.parent p
                WHERE f.deleted = true
                AND f.owner = ?1
                AND (p IS NULL OR p.deleted = false)
            """)
    List<Folder> findAllDeletedFolders(User user);

    List<Folder> findAllByDeletedTrueAndDeletedAtBefore(LocalDate threshold);
}
