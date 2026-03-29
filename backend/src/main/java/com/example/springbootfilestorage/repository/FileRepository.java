package com.example.springbootfilestorage.repository;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.security.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<UploadedFile, Long> {

    @Query("SELECT f FROM UploadedFile f WHERE f.folder IS NULL AND f.isProfilePic = false AND f.deleted = false")
    List<UploadedFile> findAllFilesWithNoFolder();

    @Query("SELECT f FROM UploadedFile f WHERE f.originalFilename = ?1 AND f.folder.id = ?2 AND  f.isProfilePic = false AND f.deleted = false")
    UploadedFile findByNameAndFolderId(String name, Long folderId);

    @Query("SELECT f FROM UploadedFile f WHERE f.folder.id = ?1 AND f.originalFilename LIKE %?2% AND f.isProfilePic = false AND f.deleted = false")
    List<UploadedFile> findByNameAndId(Long folderId, String name);

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

    @Query("SELECT f FROM UploadedFile f ORDER BY f.size DESC LIMIT 5")
    List<UploadedFile> fiveBiggestFiles();

    @Query("SELECT f FROM UploadedFile f WHERE f.owner = ?1 AND f.deleted = false")
    List<UploadedFile> findAllFiles(User authenticatedUser);
}
