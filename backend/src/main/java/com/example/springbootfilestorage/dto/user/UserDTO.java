package com.example.springbootfilestorage.dto.user;

import com.example.springbootfilestorage.security.dao.Role;

import java.util.UUID;

public record UserDTO(
        UUID uuid,
        String firstname,
        String lastname,
        String username,
        String emailaddress,
        boolean enabled,
        Role role
) {
}
