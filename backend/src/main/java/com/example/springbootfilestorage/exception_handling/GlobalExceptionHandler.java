package com.example.springbootfilestorage.exception_handling;

import com.example.springbootfilestorage.exception_handling.exceptions.LoginException;
import com.example.springbootfilestorage.exception_handling.exceptions.SignUpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

/**
 * A global exception handler for managing application-wide exceptions.
 * This class provides specific handlers for custom exceptions and enhances the application's
 * ability to deliver user-friendly error messages and appropriate HTTP responses.
 * It uses the @ControllerAdvice annotation to apply exception handling
 * across all controllers in the application.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<String> handleSignUpException(SignUpException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> loginUpException(LoginException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException e) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "File too large");
        error.put("message", "The uploaded file exceeds the maximum allowed size");

        return ResponseEntity.badRequest().body(error);
    }
}
