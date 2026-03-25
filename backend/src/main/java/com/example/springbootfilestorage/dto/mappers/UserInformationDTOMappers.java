package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.user.UserInformationDTO;
import com.example.springbootfilestorage.security.dao.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserInformationDTOMappers implements Function<User, UserInformationDTO> {
    @Override
    public UserInformationDTO apply(User user) {
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
}
