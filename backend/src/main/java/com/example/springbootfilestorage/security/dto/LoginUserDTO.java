package com.example.springbootfilestorage.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserDTO {
    private String email;
    private String password;
}
