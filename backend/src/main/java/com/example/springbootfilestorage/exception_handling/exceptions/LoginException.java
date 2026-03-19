package com.example.springbootfilestorage.exception_handling.exceptions;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}
