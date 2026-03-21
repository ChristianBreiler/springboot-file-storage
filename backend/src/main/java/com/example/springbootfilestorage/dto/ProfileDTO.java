package com.example.springbootfilestorage.dto;

import com.example.springbootfilestorage.security.dao.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ProfileDTO {
    String firstname;
    String lastname;
    String email;
    Role role;
    LocalDate createdAt;
    String profilePictureUrl;
}
