package com.example.springbootfilestorage.security.controller;

import com.example.springbootfilestorage.dto.user.UserDTO;
import com.example.springbootfilestorage.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/admin")
@RestController
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> index() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/delete/{userUuid}")
    public ResponseEntity<?> delete(@PathVariable UUID userUuid) {
        userService.deleteUser(userUuid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/toggle_timeout/{userUuid}")
    public ResponseEntity<Void> toggleTimeout(@PathVariable UUID userUuid) {
        userService.toggleTimeout(userUuid);
        return ResponseEntity.noContent().build();
    }
}
