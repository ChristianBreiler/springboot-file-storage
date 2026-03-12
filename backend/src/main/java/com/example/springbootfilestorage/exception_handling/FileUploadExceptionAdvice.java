package com.example.springbootfilestorage.exception_handling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException e) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "File too large");
        error.put("message", "The uploaded file exceeds the maximum allowed size");

        return ResponseEntity.badRequest().body(error);
    }
}
