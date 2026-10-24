package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.user.IsAdminDTO;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsAdminDTOMapperTest {

    private final IsAdminDTOMapper mapper = new IsAdminDTOMapper();

    @Test
    void apply_trueForAdminRole() {
        User user = new User();
        user.setRole(Role.ADMIN);

        assertThat(mapper.apply(user)).isEqualTo(new IsAdminDTO(true));
    }

    @Test
    void apply_falseForUserRole() {
        User user = new User();
        user.setRole(Role.USER);

        assertThat(mapper.apply(user)).isEqualTo(new IsAdminDTO(false));
    }
}
