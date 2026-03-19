package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dto.ProfileDTO;
import com.example.springbootfilestorage.dto.UserInformationDTO;
import com.example.springbootfilestorage.security.model.Role;
import com.example.springbootfilestorage.security.model.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.service.MessageService;
import com.example.springbootfilestorage.service.SettingsService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final SettingsService settingsService;

    public UserService(UserRepository userRepository,
                       MessageService messageService, SettingsService settingsService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.settingsService = settingsService;
    }

    public String registerUser(String firstname, String lastname, String emailaddress, String password) {
        if (firstname.isBlank() || lastname.isBlank() || emailaddress.isBlank() || password.isBlank())
            throw new IllegalArgumentException("All fields must be filled");


        String username = createUsername(firstname, lastname);
        if (isUsernameTaken(username)) username = createValidUserName(username);

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmailaddress(emailaddress.trim());
        user.setUsername(username);
        // TODO: Generate a random token and save it in the database
        user.setPassword(password);
        Settings settings = generateDefaultSettings();
        user.setSettings(settings);
        user.setRole(Role.USER);
        // Profile pic null at first
        user.setCreatedAt(LocalDate.now());
        userRepository.save(user);
        return username;
    }

    public List<User> getAllNonAdminUsers() {
        // TODO: Maybe do this in db somehow?
        return userRepository.findAll()
                .stream()
                .filter(u -> !u.getRole().equals(Role.ADMIN))
                .collect(Collectors.toList());

    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        userRepository.delete(user);
    }

    // public User changeUserName(String username) { }

    public UserInformationDTO getUserInformation() {
        // TODO: Get current user here
        return createUserInformationDTO(null);
    }

    private UserInformationDTO createUserInformationDTO(User user) {
        if (user == null) return null;
        return new UserInformationDTO(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmailaddress(),
                user.getRole(),
                getInitials(user.getFirstname(), user.getLastname())
        );
    }

    private String createUsername(String firstname, String lastname) {
        String first = firstname.toLowerCase().trim();
        String last = lastname.toLowerCase().trim();
        if (first.isBlank() || last.isBlank())
            throw new IllegalArgumentException("Firstname and lastname must not be empty");

        // Handle Special names like Something von Something
        String[] firstNames = first.split("\\s+");

        String firstPart = (firstNames.length == 1)
                ? String.valueOf(firstNames[0].charAt(0))
                : Arrays.stream(firstNames)
                .filter(s -> !s.isBlank())
                .map(s -> String.valueOf(s.charAt(0)))
                .collect(Collectors.joining());

        return firstPart + last;
    }

    public String getInitials(String firstname, String lastname) {
        // TODO: Get user from db
        if (firstname == null || lastname == null || firstname.isBlank() || lastname.isBlank())
            throw new IllegalArgumentException("Firstname and lastname must not be empty");

        String[] names = Stream.concat(
                Arrays.stream(firstname.split("\\s+")),
                Arrays.stream(lastname.split("\\s+"))
        ).toArray(String[]::new);

        if (names.length == 2) return (String.valueOf(names[0].charAt(0) + names[1].charAt(0))).toUpperCase();
        else {
            // Handle Special names like Something von Something
            String firstLetter = (String.valueOf(names[0].charAt(0))).toUpperCase();
            String lastLetter = (String.valueOf(names[names.length - 1].charAt(0))).toUpperCase();
            StringBuilder restLetters = new StringBuilder();
            for (int i = 1; i < names.length - 1; i++) {
                restLetters.append(String.valueOf(names[i].charAt(0)).toLowerCase());
            }
            return firstLetter + restLetters.toString() + lastLetter;
        }
    }

    private String generateApiToken() {
        byte[] bytes = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private Settings generateDefaultSettings() {
        Settings settings = new Settings();
        settings.setPageLayout(PageLayout.CARDS);
        settings.setLanguage(Language.EN);
        settings.setDeleteFilesAfterXWeeks(2);
        settingsService.saveSettings(settings);
        return settings;
    }

    private String createValidUserName(String baseName) {
        if (baseName == null || baseName.isBlank()) throw new IllegalArgumentException("Username cannot be empty");

        final int MAX_ATTEMPTS = 100;
        String candidate = baseName;
        for (int i = 1; isUsernameTaken(candidate); i++) {
            candidate = baseName + "_" + i;
            if (i > MAX_ATTEMPTS) {
                throw new IllegalStateException("Failed to generate unique username");
            }
        }
        return candidate;
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public ProfileDTO getProfile() {
        return null;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
}