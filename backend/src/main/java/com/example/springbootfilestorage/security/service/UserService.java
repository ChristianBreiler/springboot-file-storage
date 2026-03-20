package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.dto.ProfileDTO;
import com.example.springbootfilestorage.dto.UserInformationDTO;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserContext userContext;

    public UserService(UserRepository userRepository, UserContext userContext) {
        this.userRepository = userRepository;
        this.userContext = userContext;
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

    public User changeUserName(String username) {
        User user = userContext.getAuthenticatedUser();
        user.setUsername(username);
        userRepository.save(user);
        return user;
    }

    public UserInformationDTO getUserInformation() {
        return createUserInformationDTO(userContext.getAuthenticatedUser());
    }

    private UserInformationDTO createUserInformationDTO(User user) {
        if (user == null) return null;
        return new UserInformationDTO(
                user.getFirstname() + " " + user.getLastname(),
                user.getRole(),
                user.getProfilePic().getStoragePath()
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
        return createProfileDTO(userContext.getAuthenticatedUser());
    }

    private ProfileDTO createProfileDTO(User user) {
        return new ProfileDTO(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmailaddress(),
                user.getRole(),
                user.getProfilePic().getStoredName()
        );
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
}