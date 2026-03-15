package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.UserInformationDTO;
import com.example.springbootfilestorage.security.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInformationDTO> userInformation(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(userService.getUserInformation(id));
    }
}
