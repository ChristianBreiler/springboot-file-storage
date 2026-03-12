package com.example.springbootfilestorage.security.user;

import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dao.UploadedFile;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String emailaddress;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "profile_pic_id")
    private UploadedFile profilePic;

    private Role role;

    @OneToOne
    @JoinColumn(name = "settings_id")
    private Settings settings;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;
}
