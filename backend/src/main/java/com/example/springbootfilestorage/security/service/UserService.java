package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.dto.ProfileDTO;
import com.example.springbootfilestorage.dto.UserInformationDTO;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserContext userContext;

    public UserService(UserRepository userRepository, UserContext userContext) {
        this.userRepository = userRepository;
        this.userContext = userContext;
    }

    public UserInformationDTO getUserInformation() {
        return createUserInformationDTO(userContext.getAuthenticatedUser());
    }

    private UserInformationDTO createUserInformationDTO(User user) {
        if (user == null) return null;
        String profilePicName = null;
        if (user.getProfilePic() != null) profilePicName = user.getProfilePic().getStoredName();
        return new UserInformationDTO(
                user.getFirstname(),
                user.getLastname(),
                user.getRole(),
                profilePicName
        );
    }

    public ProfileDTO getProfile() {
        return createProfileDTO(userContext.getAuthenticatedUser());
    }

    private ProfileDTO createProfileDTO(User user) {
        String profilePicName = null;
        if (user.getProfilePic() != null) profilePicName = user.getProfilePic().getStoredName();
        return new ProfileDTO(
                user.getFirstname(),
                user.getLastname(),
                user.getEmailaddress(),
                user.getRole(),
                user.getCreatedAt(),
                profilePicName
        );
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
}