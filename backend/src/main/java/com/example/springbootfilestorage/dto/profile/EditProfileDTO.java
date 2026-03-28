package com.example.springbootfilestorage.dto.profile;

public record EditProfileDTO(
        String firstname,
        String lastname,
        String email,
        String profilePictureUrl
) {
}
