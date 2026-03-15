package com.example.springbootfilestorage.dto;

import com.example.springbootfilestorage.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInformationDTO {
    String username;
    String firstname;
    String lastname;
    String email;
    Role role;
    String initials;
    // TODO: Profile pic
}
