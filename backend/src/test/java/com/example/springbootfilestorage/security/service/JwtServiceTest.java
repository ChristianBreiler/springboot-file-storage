package com.example.springbootfilestorage.security.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String SECRET_KEY = "k3Vq9xT2mZp7LwR4sYb1NfJ8hDc6UeA0gXoQ5iHtMvE=";
    private static final long EXPIRATION_MS = 3_600_000L;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION_MS);
    }

    private UserDetails userDetails(String username) {
        return User.withUsername(username).password("pw").authorities("ROLE_USER").build();
    }

    @Test
    void generateToken_allowsExtractingTheUsernameBack() {
        UserDetails details = userDetails("john@example.com");

        String token = jwtService.generateToken(details, false);

        assertThat(jwtService.extractUsername(token)).isEqualTo("john@example.com");
    }

    @Test
    void isTokenValid_trueForMatchingUnexpiredToken() {
        UserDetails details = userDetails("jane@example.com");

        String token = jwtService.generateToken(details, false);

        assertThat(jwtService.isTokenValid(token, details)).isTrue();
    }

    @Test
    void isTokenValid_falseWhenUsernameDiffers() {
        UserDetails original = userDetails("jane@example.com");
        UserDetails other = userDetails("other@example.com");

        String token = jwtService.generateToken(original, false);

        assertThat(jwtService.isTokenValid(token, other)).isFalse();
    }

    @Test
    void getExpirationTime_returnsConfiguredValue() {
        assertThat(jwtService.getExpirationTime()).isEqualTo(EXPIRATION_MS);
    }

    @Test
    void generateToken_rememberMe_producesValidToken() {
        UserDetails details = userDetails("remember@example.com");

        String token = jwtService.generateToken(details, true);

        assertThat(jwtService.isTokenValid(token, details)).isTrue();
    }

    @Test
    void expiredToken_isRejectedWhileParsing() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        UserDetails details = userDetails("expired@example.com");

        String token = jwtService.generateToken(details, false);

        assertThatThrownBy(() -> jwtService.isTokenValid(token, details))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
