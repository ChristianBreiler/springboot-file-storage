package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.user.UserInformationDTO;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserInformationDTOMappersTest {

    private final UserInformationDTOMappers mapper = new UserInformationDTOMappers();

    @Test
    void apply_returnsNull_whenUserIsNull() {
        assertThat(mapper.apply(null)).isNull();
    }

    @Test
    void apply_mapsNameAndRole() {
        User user = new User();
        user.setFirstname("Jane");
        user.setLastname("Smith");
        user.setRole(Role.USER);

        UserInformationDTO result = mapper.apply(user);

        assertThat(result).isEqualTo(new UserInformationDTO("Jane", "Smith", Role.USER));
    }
}
