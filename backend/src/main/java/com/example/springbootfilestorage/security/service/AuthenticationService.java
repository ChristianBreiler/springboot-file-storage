package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.exception_handling.exceptions.LoginException;
import com.example.springbootfilestorage.exception_handling.exceptions.SignUpException;
import com.example.springbootfilestorage.mail.EmailService;
import com.example.springbootfilestorage.security.dto.LoginUserDTO;
import com.example.springbootfilestorage.security.dto.RegisterUserDTO;
import com.example.springbootfilestorage.security.model.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signUp(RegisterUserDTO input) {
        if (isEmailaddressTaken(input.getEmail())) throw new SignUpException("Email address already taken");

        User user = new User();
        user.setFirstname(input.getFirstname());
        user.setLastname(input.getLastname());
        // TODO: Change this
        user.setUsername(input.getEmail());
        user.setEmailaddress(input.getEmail());
        user.setCreatedAt(LocalDate.now());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));
        // TODO: Should be false because of verification email not verified
        user.setEnabled(true);
        // TODO: Email
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new SignUpException("Failed to sign up user " + e.getMessage());
        }
    }

    public User authenticate(LoginUserDTO input) {
        User user = userRepository.findByEmailaddress(input.getEmail());
        if (user == null) throw new LoginException("Invalid email address");

        if (!user.isEnabled()) throw new RuntimeException("User not verified");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return user;
    }

    // TODO: Verify email method

    // TODO: With resend

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    private boolean isEmailaddressTaken(String emailaddress) {
        return userRepository.isEmailaddressTaken(emailaddress.toLowerCase().trim());
    }
}
