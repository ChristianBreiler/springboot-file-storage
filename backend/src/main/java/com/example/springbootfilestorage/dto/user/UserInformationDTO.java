package com.example.springbootfilestorage.dto.user;

import com.example.springbootfilestorage.security.dao.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInformationDTO {
    String firstname;
    String lastname;
    Role role;
    String profilePictureUrl;
}
