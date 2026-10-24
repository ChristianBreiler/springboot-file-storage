package com.example.springbootfilestorage.security.service;

import com.example.springbootfilestorage.exception_handling.exceptions.SignUpException;
import com.example.springbootfilestorage.security.dao.Role;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.dto.LoginUserDTO;
import com.example.springbootfilestorage.security.dto.RegisterUserDTO;
import com.example.springbootfilestorage.security.repository.UserRepository;
import com.example.springbootfilestorage.security.response.LoginResponse;
import com.example.springbootfilestorage.service.SettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private SettingsService settingsService;
    @Mock
    private JwtService jwtService;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, passwordEncoder, authenticationManager,
                settingsService, jwtService);
    }

    @Test
    void signUp_createsNormalizedEnabledUser() {
        RegisterUserDTO input = new RegisterUserDTO("NEW@example.com", "pw", "jOHN", "dOE");
        when(userRepository.isEmailaddressTaken("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("pw")).thenReturn("encoded-pw");

        boolean result = authenticationService.signUp(input);

        assertThat(result).isTrue();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getFirstname()).isEqualTo("John");
        assertThat(saved.getLastname()).isEqualTo("Doe");
        assertThat(saved.getUsername()).isEqualTo("NEW@example.com");
        assertThat(saved.getEmailaddress()).isEqualTo("NEW@example.com");
        assertThat(saved.getPassword()).isEqualTo("encoded-pw");
        assertThat(saved.getRole()).isEqualTo(Role.USER);
        assertThat(saved.isEnabled()).isTrue();
        assertThat(saved.getSettings()).isNotNull();
        verify(settingsService).saveSettings(saved.getSettings());
    }

    @Test
    void signUp_emailAlreadyTaken_throwsAndDoesNotSave() {
        RegisterUserDTO input = new RegisterUserDTO("taken@example.com", "pw", "John", "Doe");
        when(userRepository.isEmailaddressTaken("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authenticationService.signUp(input))
                .isInstanceOf(SignUpException.class)
                .hasMessageContaining("already taken");

        verify(userRepository, never()).save(any());
    }

    @Test
    void signUp_repositoryFailure_isWrappedInSignUpException() {
        RegisterUserDTO input = new RegisterUserDTO("new@example.com", "pw", "John", "Doe");
        when(userRepository.isEmailaddressTaken("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("pw")).thenReturn("encoded-pw");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> authenticationService.signUp(input))
                .isInstanceOf(SignUpException.class)
                .hasMessageContaining("Failed to register user");
    }

    @Test
    void authenticate_returnsTokenAndEnabledFlagFromPrincipal() {
        LoginUserDTO input = new LoginUserDTO("jane@example.com", "pw", true);
        User principal = new User();
        principal.setEnabled(true);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(principal, true)).thenReturn("jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3_600_000L);

        LoginResponse response = authenticationService.authenticate(input);

        assertThat(response).isEqualTo(new LoginResponse("jwt-token", 3_600_000L, true));
    }
}
