package com.example.springbootfilestorage.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class StorageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long usedSpace() {
        return entityManager.createQuery(
                "SELECT SUM(f.size) FROM UploadedFile f",
                Long.class
        ).getSingleResult();
    }
}
