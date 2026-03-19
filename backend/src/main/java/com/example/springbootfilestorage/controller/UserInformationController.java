package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.UserInformationDTO;
import com.example.springbootfilestorage.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userinformation")
public class UserInformationController {

    private final UserService userService;

    public UserInformationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<UserInformationDTO> userInformation() {
        return ResponseEntity.ok(userService.getUserInformation());
    }
}
