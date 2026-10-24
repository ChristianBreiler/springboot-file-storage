package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.user.UserDTO;
import com.example.springbootfilestorage.security.dao.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getUuid(),
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getEmailaddress(),
                user.isEnabled(),
                user.getRole()
        );
    }
}
