package com.example.springbootfilestorage.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// TODO: Used in emails

@Data
@AllArgsConstructor
public class VerifyUserDTO {
    private String email;
    private String verificationCode;
}
