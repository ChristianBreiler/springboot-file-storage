package com.example.springbootfilestorage.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Used in emails

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyUserDTO {
    private String email;
    private String verificationCode;
}
