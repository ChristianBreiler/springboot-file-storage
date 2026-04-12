package com.example.springbootfilestorage.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

// Use class here instead of record since record caused problems with the profilePic image

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileDTO {
    private String firstname;
    private String lastname;
    private String email;
}
