package com.example.springbootfilestorage.dto.profile;

import org.springframework.web.multipart.MultipartFile;

public record EditProfileDTO(
        String firstname,
        String lastname,
        String email,
        MultipartFile profilePic
) {
}
