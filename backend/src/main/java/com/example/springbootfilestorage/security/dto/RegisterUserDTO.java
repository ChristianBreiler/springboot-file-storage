package com.example.springbootfilestorage.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserDTO {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}
