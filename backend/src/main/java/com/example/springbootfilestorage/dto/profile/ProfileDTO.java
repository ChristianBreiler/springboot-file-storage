package com.example.springbootfilestorage.dto.profile;

import com.example.springbootfilestorage.security.dao.Role;

import java.time.LocalDate;

public record ProfileDTO(
        String firstname,
        String lastname,
        String email,
        Role role,
        LocalDate createdAt,
        String profilePictureUrl
) {
}
