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
        try {
            Long result = entityManager.createQuery(
                            "SELECT SUM(f.size) FROM UploadedFile f WHERE f.owner = :user AND f.deleted = false",
                            Long.class
                    )
                    .setParameter("user", user)
                    .getSingleResult();

            return result != null ? result : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }
}
