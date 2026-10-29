package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.dto.mappers.IsAdminDTOMapper;
import com.example.springbootfilestorage.dto.mappers.ProfileDTOMapper;
import com.example.springbootfilestorage.dto.mappers.UserDTOMapper;
import com.example.springbootfilestorage.dto.mappers.UserInformationDTOMappers;
import com.example.springbootfilestorage.dto.profile.EditProfileDTO;
import com.example.springbootfilestorage.dto.profile.ProfileDTO;
import com.example.springbootfilestorage.dto.user.IsAdminDTO;
import com.example.springbootfilestorage.dto.user.UserInformationDTO;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserContext userContext;

    private UserService userService;
    private User authenticatedUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userContext, new IsAdminDTOMapper(),
                new ProfileDTOMapper(), new UserInformationDTOMappers(), new UserDTOMapper());

        authenticatedUser = new User();
        authenticatedUser.setUuid(UUID.randomUUID());
        authenticatedUser.setFirstname("John");
        authenticatedUser.setLastname("Doe");
        authenticatedUser.setEmailaddress("john@example.com");
        authenticatedUser.setRole(Role.USER);
        authenticatedUser.setCreatedAt(LocalDate.of(2024, 1, 1));
        authenticatedUser.setEnabled(true);
    }

    @Test
    void getUserInformation_mapsAuthenticatedUser() {
        when(userContext.getAuthenticatedUser()).thenReturn(authenticatedUser);

        UserInformationDTO result = userService.getUserInformation();

        assertThat(result).isEqualTo(new UserInformationDTO("John", "Doe", Role.USER));
    }

    @Test
    void getProfile_mapsAuthenticatedUser() {
        when(userContext.getAuthenticatedUser()).thenReturn(authenticatedUser);

        ProfileDTO result = userService.getProfile();

        assertThat(result).isEqualTo(new ProfileDTO("John", "Doe", "john@example.com", Role.USER,
                LocalDate.of(2024, Month.JANUARY, 1)));
    }

    @Test
    void isAdmin_falseForRegularUser() {
        when(userContext.getAuthenticatedUser()).thenReturn(authenticatedUser);

        assertThat(userService.isAdmin()).isEqualTo(new IsAdminDTO(false));
    }

    @Test
    void isAdmin_trueForAdminUser() {
        authenticatedUser.setRole(Role.ADMIN);
        when(userContext.getAuthenticatedUser()).thenReturn(authenticatedUser);

        assertThat(userService.isAdmin()).isEqualTo(new IsAdminDTO(true));
    }

    @Test
    void editProfile_updatesAndSavesAuthenticatedUser() {
        when(userContext.getAuthenticatedUser()).thenReturn(authenticatedUser);
        EditProfileDTO edit = new EditProfileDTO("Jane", "Smith", "jane@example.com");

        ProfileDTO result = userService.editProfile(edit);

        assertThat(authenticatedUser.getFirstname()).isEqualTo("Jane");
        assertThat(authenticatedUser.getLastname()).isEqualTo("Smith");
        assertThat(authenticatedUser.getEmailaddress()).isEqualTo("jane@example.com");
        verify(userRepository).save(authenticatedUser);
        assertThat(result.firstname()).isEqualTo("Jane");
        assertThat(result.email()).isEqualTo("jane@example.com");
    }

    @Test
    void timeOutUser_disablesAndSavesUser() {
        UUID uuid = authenticatedUser.getUuid();
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(authenticatedUser));

        boolean currentEnabled = authenticatedUser.isEnabled();
        userService.toggleTimeout(uuid);

        assertThat(authenticatedUser.isEnabled() != currentEnabled).isTrue();
        verify(userRepository).save(authenticatedUser);
    }

    @Test
    void timeOutUser_userNotFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.toggleTimeout(uuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
        verify(userRepository, never()).save(any());
    }
}
