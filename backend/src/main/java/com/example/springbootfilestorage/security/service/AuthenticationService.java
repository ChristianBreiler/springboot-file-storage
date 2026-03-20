package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.exception_handling.exceptions.LoginException;
import com.example.springbootfilestorage.exception_handling.exceptions.SignUpException;
import com.example.springbootfilestorage.mail.EmailService;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dto.LoginUserDTO;
import com.example.springbootfilestorage.security.dto.RegisterUserDTO;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.service.SettingsService;
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
    private final SettingsService settingsService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, EmailService emailService,
                                 SettingsService settingsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.settingsService = settingsService;
    }

    public User signUp(RegisterUserDTO input) {
        if (isEmailaddressTaken(input.getEmail())) throw new SignUpException("Email address already taken");

        User user = new User();
        user.setFirstname(handleName(input.getFirstname()));
        user.setLastname(handleName(input.getLastname()));
        // TODO: Change this
        user.setUsername(input.getEmail());
        user.setEmailaddress(input.getEmail());
        user.setCreatedAt(LocalDate.now());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));
        user.setSettings(generateDefaultSettings());
        user.setRole(Role.USER);
        // TODO: Should be false because of verification email not verified
        user.setEnabled(true);
        // TODO: Email
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new SignUpException("Failed to register user " + e.getMessage());
        }
    }

    // Make names uniform, since it's not checked in frontend
    private String handleName(String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) return "";
        return trimmed.substring(0, 1).toUpperCase() +
                trimmed.substring(1).toLowerCase();
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

    private Settings generateDefaultSettings() {
        Settings settings = new Settings();
        settings.setPageLayout(PageLayout.CARDS);
        settings.setLanguage(Language.EN);
        settings.setDeleteFilesAfterXWeeks(2);
        settingsService.saveSettings(settings);
        return settings;
    }
}
