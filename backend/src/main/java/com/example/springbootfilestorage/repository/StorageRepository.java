package com.example.springbootfilestorage.repository;

import com.example.springbootfilestorage.dto.storage.StorageDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Long, Long> {

    // TODO: specify user here
    @Query("SELECT SUM(f.size) FROM UploadedFile f")
    Long usedSpace();

    @Query("SELECT COUNT(f) FROM UploadedFile f")
    StorageDetailDTO storageDetails();
}
