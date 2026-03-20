package com.example.springbootfilestorage.dto;

import com.example.springbootfilestorage.security.dao.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInformationDTO {
    String fullName;
    Role role;
    String profilePictureUrl;
}
