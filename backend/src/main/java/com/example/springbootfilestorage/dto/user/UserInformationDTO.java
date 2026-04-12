package com.example.springbootfilestorage.dto.user;

import com.example.springbootfilestorage.security.dao.Role;

public record UserInformationDTO(
        String firstname,
        String lastname,
        Role role
) {
}
