package com.example.springbootfilestorage.security.response;

public record LoginResponse(
        String token,
        long expiresIn,
        // Do this to check if a verification email has to be send
        boolean isEnabled
) {
}
