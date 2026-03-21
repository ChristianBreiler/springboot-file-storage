package com.example.springbootfilestorage.security.controller;

import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.dto.LoginUserDTO;
import com.example.springbootfilestorage.security.dto.RegisterUserDTO;
import com.example.springbootfilestorage.security.response.LoginResponse;
import com.example.springbootfilestorage.security.service.AuthenticationService;
import com.example.springbootfilestorage.security.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        if (authenticationService.signUp(registerUserDTO)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDTO loginUserDTO) {
        User authenticatedUser = authenticationService.authenticate(loginUserDTO);
        String jwt = jwtService.generateToken(authenticatedUser);
        LoginResponse response = new LoginResponse(jwt, jwtService.getExpirationTime());
        return ResponseEntity.ok(response);
    }
}
