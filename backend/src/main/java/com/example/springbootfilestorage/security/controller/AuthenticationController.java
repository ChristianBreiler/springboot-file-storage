package com.example.springbootfilestorage.security.controller;

import com.example.springbootfilestorage.security.dto.LoginUserDTO;
import com.example.springbootfilestorage.security.dto.RegisterUserDTO;
import com.example.springbootfilestorage.security.response.LoginResponse;
import com.example.springbootfilestorage.security.service.AuthenticationService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Limit: max 10 Requests per minute
        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
    }

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO, HttpServletRequest request) {
        if (ratelimitExceeded(request)) return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests");

        if (authenticationService.signUp(registerUserDTO)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO, HttpServletRequest request) {
        if (ratelimitExceeded(request)) return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests");

        LoginResponse response = authenticationService.authenticate(loginUserDTO);
        return ResponseEntity.ok(response);
    }

    private boolean ratelimitExceeded(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

        return !bucket.tryConsume(1);
    }
}
