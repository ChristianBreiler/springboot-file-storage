package com.example.springbootfilestorage.dto;

import com.example.springbootfilestorage.security.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {
    String username;
    String firstname;
    String lastname;
    String email;
    Role role;
    String profilePictureUrl;
}
