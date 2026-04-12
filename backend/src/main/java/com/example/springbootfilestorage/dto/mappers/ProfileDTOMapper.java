package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.profile.ProfileDTO;
import com.example.springbootfilestorage.security.dao.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProfileDTOMapper implements Function<User, ProfileDTO> {
    @Override
    public ProfileDTO apply(User user) {
        return new ProfileDTO(
                user.getFirstname(),
                user.getLastname(),
                user.getEmailaddress(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
