package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dto.systemsettings.CanRegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class SystemSettingsServiceTest {

    private final SystemSettingsService systemSettingsService = new SystemSettingsService();

    @Test
    void canRegister_enabled_returnsTrue() {
        ReflectionTestUtils.setField(systemSettingsService, "canRegister", true);

        assertThat(systemSettingsService.canRegister()).isEqualTo(new CanRegisterDTO(true));
    }

    @Test
    void canRegister_disabled_returnsFalse() {
        ReflectionTestUtils.setField(systemSettingsService, "canRegister", false);

        assertThat(systemSettingsService.canRegister()).isEqualTo(new CanRegisterDTO(false));
    }
}
