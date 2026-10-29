package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.dto.mappers.IsAdminDTOMapper;
import com.example.springbootfilestorage.dto.mappers.ProfileDTOMapper;
import com.example.springbootfilestorage.dto.mappers.UserDTOMapper;
import com.example.springbootfilestorage.dto.mappers.UserInformationDTOMappers;
import com.example.springbootfilestorage.dto.profile.EditProfileDTO;
import com.example.springbootfilestorage.dto.profile.ProfileDTO;
import com.example.springbootfilestorage.dto.user.IsAdminDTO;
import com.example.springbootfilestorage.dto.user.UserDTO;
import com.example.springbootfilestorage.dto.user.UserInformationDTO;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserContext userContext;
    private final IsAdminDTOMapper isAdminDTOMapper;
    private final ProfileDTOMapper profileDTOMapper;
    private final UserInformationDTOMappers userInformationDTOMappers;
    private final UserDTOMapper userDTOMapper;

    public UserService(UserRepository userRepository, UserContext userContext, IsAdminDTOMapper isAdminDTOMapper,
                       ProfileDTOMapper profileDTOMapper, UserInformationDTOMappers userInformationDTOMappers, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userContext = userContext;
        this.isAdminDTOMapper = isAdminDTOMapper;
        this.profileDTOMapper = profileDTOMapper;
        this.userInformationDTOMappers = userInformationDTOMappers;
        this.userDTOMapper = userDTOMapper;
    }

    public UserInformationDTO getUserInformation() {
        return userInformationDTOMappers.apply(userContext.getAuthenticatedUser());
    }

    public ProfileDTO getProfile() {
        return profileDTOMapper.apply(userContext.getAuthenticatedUser());
    }

    public IsAdminDTO isAdmin() {
        return isAdminDTOMapper.apply(userContext.getAuthenticatedUser());
    }

    public ProfileDTO editProfile(EditProfileDTO editedProfile) {
        User user = userContext.getAuthenticatedUser();
        user.setFirstname(editedProfile.getFirstname());
        user.setLastname(editedProfile.getLastname());
        user.setEmailaddress(editedProfile.getEmail());
        userRepository.save(user);
        return profileDTOMapper.apply(user);
    }

    public void toggleTimeout(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() != Role.ADMIN)
                .map(userDTOMapper)
                .toList();
    }

    public void deleteUser(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new RuntimeException("User not found"));
        // TODO: Delete the user resources too
        userRepository.delete(user);
    }
}