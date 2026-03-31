package com.example.springbootfilestorage.repository;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.security.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<UploadedFile, Long> {

    @Query("SELECT f FROM UploadedFile f WHERE f.folder IS NULL AND f.isProfilePic = false AND f.deleted = false AND f.owner = ?1")
    List<UploadedFile> findAllFilesWithNoFolder(User currentUser);

    @Query("SELECT f FROM UploadedFile f WHERE f.folder.uuid = ?1 AND f.originalFilename LIKE %?2% AND f.isProfilePic = false AND f.deleted = false")
    List<UploadedFile> findByNameAndId(UUID folderUuid, String name);

    @Query("SELECT f FROM UploadedFile f WHERE f.folder IS NULL AND f.originalFilename LIKE %?1% AND f.isProfilePic = false AND f.deleted = false")
    List<UploadedFile> findFilesByNameOnHomePage(String name);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM UploadedFile f WHERE f.storagePath = ?1 AND f.deleted = false")
    boolean physicalFileExists(String path);

    @Query("SELECT f FROM UploadedFile f WHERE f.fileShareCode = ?1 AND f.deleted = false")
    UploadedFile finaByFileShareCode(String fileShareCode);

    @Query("SELECT f FROM UploadedFile f WHERE f.deleted = true AND f.owner = ?1")
    List<UploadedFile> findAllDeletedFiles(User authenticatedUser);

    @Query("SELECT f FROM UploadedFile f WHERE f.deleted = true AND f.finalDeletionDate <= ?1")
    List<UploadedFile> findAllFilesToBeDeletedToday(LocalDate now);

    @Query("SELECT f.storagePath FROM UploadedFile f WHERE f.isProfilePic = false AND f.owner.id = ?1 AND f.deleted = false")
    String findStoragePath(Long id);

    @Query("SELECT COUNT(f) FROM UploadedFile f WHERE f.deleted = false")
    int numberOfFiles();

    @Query("SELECT f FROM UploadedFile f WHERE f.owner = ?1 ORDER BY f.size DESC LIMIT 5")
    List<UploadedFile> fiveBiggestFiles(User authenticatedUser);

    @Query("SELECT f FROM UploadedFile f WHERE f.owner = ?1 AND f.deleted = false")
    List<UploadedFile> findAllFiles(User authenticatedUser);

    @Query("SELECT f FROM UploadedFile f WHERE f.uuid = ?1")
    Optional<UploadedFile> findByUuid(UUID uuid);

    @Query("SELECT f FROM UploadedFile f WHERE  f.originalFilename LIKE %?1% AND f.folder.uuid = ?2 AND f.isProfilePic = false AND f.deleted = false")
    UploadedFile findByNameAndFolderUuid(String name, UUID folderUuid);
}
