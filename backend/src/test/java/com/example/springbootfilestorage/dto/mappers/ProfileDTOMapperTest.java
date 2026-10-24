package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dto.profile.ProfileDTO;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileDTOMapperTest {

    private final ProfileDTOMapper mapper = new ProfileDTOMapper();

    @Test
    void apply_mapsUserFieldsToProfileDTO() {
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmailaddress("john@example.com");
        user.setRole(Role.ADMIN);
        user.setCreatedAt(LocalDate.of(2023, 3, 10));

        ProfileDTO result = mapper.apply(user);

        assertThat(result).isEqualTo(new ProfileDTO("John", "Doe", "john@example.com", Role.ADMIN,
                LocalDate.of(2023, 3, 10)));
    }
}
