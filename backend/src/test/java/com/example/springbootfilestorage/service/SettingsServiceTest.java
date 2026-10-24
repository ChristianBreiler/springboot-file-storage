package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dto.mappers.SettingsDTOMapper;
import com.example.springbootfilestorage.dto.settings.SettingsDTO;
import com.example.springbootfilestorage.repository.SettingsRepository;
import com.example.springbootfilestorage.security.dao.User;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {

    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private UserContext userContext;

    private SettingsService settingsService;
    private Settings settings;

    @BeforeEach
    void setUp() {
        settingsService = new SettingsService(settingsRepository, userContext, new SettingsDTOMapper());

        settings = new Settings();
        settings.setPageLayout(PageLayout.LIST);
        settings.setLanguage(Language.EN);

        User user = new User();
        user.setSettings(settings);
        lenient().when(userContext.getAuthenticatedUser()).thenReturn(user);
    }

    @Test
    void getSettingsFromUser_mapsCurrentUserSettings() {
        SettingsDTO result = settingsService.getSettingsFromUser();

        assertThat(result).isEqualTo(new SettingsDTO("list", "en"));
    }

    @Test
    void saveSettings_delegatesToRepository() {
        settingsService.saveSettings(settings);

        verify(settingsRepository).save(settings);
    }

    @Test
    void updateSettings_updatesLanguageAndLayoutCaseInsensitively() {
        SettingsDTO result = settingsService.updateSettings(new SettingsDTO("CARDS", "DE"));

        assertThat(settings.getPageLayout()).isEqualTo(PageLayout.CARDS);
        assertThat(settings.getLanguage()).isEqualTo(Language.DE);
        verify(settingsRepository).save(settings);
        assertThat(result).isEqualTo(new SettingsDTO("cards", "de"));
    }

    @Test
    void updateSettings_invalidLanguage_throws() {
        assertThatThrownBy(() -> settingsService.updateSettings(new SettingsDTO("list", "fr")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid language");
    }

    @Test
    void updateSettings_invalidPageLayout_throws() {
        assertThatThrownBy(() -> settingsService.updateSettings(new SettingsDTO("grid", "en")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid page layout");
    }
}
