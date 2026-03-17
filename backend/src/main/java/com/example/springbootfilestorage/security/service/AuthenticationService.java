package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.mail.EmailService;
import com.example.springbootfilestorage.security.dto.LoginUserDTO;
import com.example.springbootfilestorage.security.dto.RegisterUserDTO;
import com.example.springbootfilestorage.security.model.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        User user = new User();
        user.setFirstname(input.getFirstname());
        user.setLastname(input.getLastname());
        user.setEmailaddress(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerifificationCode());
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));
        // TODO: Should be false because of verification email not verified
        user.setEnabled(true);
        // TODO: Email
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDTO input) {
        User user = userRepository.findByEmailaddress(input.getEmail());
        if (user == null) throw new RuntimeException("User not found");

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

    private String generateVerifificationCode() {
        return null;
    }
}
