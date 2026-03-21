package com.example.springbootfilestorage.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message extends BaseDAO {

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDate createdAt;
}
