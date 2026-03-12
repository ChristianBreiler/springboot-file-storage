package com.example.springbootfilestorage.repository;

import com.example.springbootfilestorage.dao.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.createdAt >= ?1")
    List<Message> findAllByCreatedAtAfter(LocalDate date);

    @Query("SELECT m FROM Message m ORDER BY m.createdAt DESC")
    List<Message> findAllMessages();

    @Query("SELECT m FROM Message m ORDER BY m.createdAt DESC LIMIT ?1")
    List<Message> findAllMessagesWithLimit(int limit);
}
