package com.example.springbootfilestorage.repository;

import com.example.springbootfilestorage.security.dao.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class StorageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long usedSpace(User user) {
        return entityManager.createQuery(
                "SELECT SUM(f.size) FROM UploadedFile f WHERE f.owner = :user AND f.deleted = false",
                Long.class
        ).getSingleResult();
    }
}
