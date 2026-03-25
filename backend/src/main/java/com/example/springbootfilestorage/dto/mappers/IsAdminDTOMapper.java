package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.user.IsAdminDTO;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IsAdminDTOMapper implements Function<User, IsAdminDTO> {
    @Override
    public IsAdminDTO apply(User user) {
        return new IsAdminDTO(user.getRole().equals(Role.ADMIN));
    }
}
