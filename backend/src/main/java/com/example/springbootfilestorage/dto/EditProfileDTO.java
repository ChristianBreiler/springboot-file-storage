package com.example.springbootfilestorage.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditProfileDTO {
    String firstname;
    String lastname;
    String email;
    String profilePictureUrl;
}
