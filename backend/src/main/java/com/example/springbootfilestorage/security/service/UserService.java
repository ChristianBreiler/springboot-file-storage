package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dto.mappers.IsAdminDTOMapper;
import com.example.springbootfilestorage.dto.mappers.ProfileDTOMapper;
import com.example.springbootfilestorage.dto.mappers.UserInformationDTOMappers;
import com.example.springbootfilestorage.dto.profile.EditProfileDTO;
import com.example.springbootfilestorage.dto.profile.ProfileDTO;
import com.example.springbootfilestorage.dto.user.IsAdminDTO;
import com.example.springbootfilestorage.dto.user.UserInformationDTO;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserContext userContext;
    private final IsAdminDTOMapper isAdminDTOMapper;
    private final ProfileDTOMapper profileDTOMapper;
    private final UserInformationDTOMappers userInformationDTOMappers;

    public UserService(UserRepository userRepository, UserContext userContext, IsAdminDTOMapper isAdminDTOMapper,
                       ProfileDTOMapper profileDTOMapper, UserInformationDTOMappers userInformationDTOMappers) {
        this.userRepository = userRepository;
        this.userContext = userContext;
        this.isAdminDTOMapper = isAdminDTOMapper;
        this.profileDTOMapper = profileDTOMapper;
        this.userInformationDTOMappers = userInformationDTOMappers;
    }

    public UserInformationDTO getUserInformation() {
        return userInformationDTOMappers.apply(userContext.getAuthenticatedUser());
    }

    public ProfileDTO getProfile() {
        return profileDTOMapper.apply(userContext.getAuthenticatedUser());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public IsAdminDTO isAdmin() {
        return isAdminDTOMapper.apply(userContext.getAuthenticatedUser());
    }

    public ProfileDTO editProfile(EditProfileDTO editedProfile) {
        User user = userContext.getAuthenticatedUser();
        user.setFirstname(editedProfile.firstname());
        user.setLastname(editedProfile.lastname());
        user.setEmailaddress(editedProfile.email());
        user.setProfilePic(initializeUploadedProfilePic(editedProfile.profilePic()));
        userRepository.save(user);
        return profileDTOMapper.apply(user);
    }

    private UploadedFile initializeUploadedProfilePic(MultipartFile file) {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setOriginalFilename(file.getOriginalFilename());
        uploadedFile.setStoredName(file.getOriginalFilename());
        uploadedFile.setSize(file.getSize());
        uploadedFile.setOwner(userContext.getAuthenticatedUser());
        uploadedFile.setProfilePic(true);
        return uploadedFile;
    }
}