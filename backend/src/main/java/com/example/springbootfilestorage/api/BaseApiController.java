package com.example.springbootfilestorage.api;

import com.example.springbootfilestorage.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

public abstract class BaseApiController {

    private final UserService userService;

    protected BaseApiController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public ResponseEntity checkToken(@RequestHeader(value = "Authorization", required = false) String header) {
        if (header == null || !header.startsWith("Bearer ")
                || userService.tokenValid(header.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }
}
