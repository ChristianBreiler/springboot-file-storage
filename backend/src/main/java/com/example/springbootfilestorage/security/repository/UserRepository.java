package com.example.springbootfilestorage.security.repository;

import com.example.springbootfilestorage.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = ?1")
    boolean existsByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.emailaddress = ?1")
    boolean isEmailaddressTaken(String emailaddress);

    @Query("SELECT u FROM User u WHERE u.emailaddress = ?1")
    User findByEmailaddress(String email);
}
