package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dto.mappers.SettingsDTOMapper;
import com.example.springbootfilestorage.dto.settings.LanguageDTO;
import com.example.springbootfilestorage.dto.settings.SettingsDTO;
import com.example.springbootfilestorage.repository.SettingsRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final UserContext userContext;
    private final SettingsDTOMapper settingsDTOMapper;

    public SettingsService(SettingsRepository settingsRepository, UserContext userContext,
                           SettingsDTOMapper settingsDTOMapper) {
        this.settingsRepository = settingsRepository;
        this.userContext = userContext;
        this.settingsDTOMapper = settingsDTOMapper;
    }

    public SettingsDTO getSettingsFromUser() {
        return settingsDTOMapper.apply(userContext.getAuthenticatedUser().getSettings());
    }

    public void saveSettings(Settings settings) {
        settingsRepository.save(settings);
    }

    public SettingsDTO updateSettings(SettingsDTO settingsDTO) {
        try {
            Settings userSettings = userContext.getAuthenticatedUser().getSettings();
            userSettings.setPageLayout(getPageLayout(settingsDTO.pageLayout()));
            userSettings.setLanguage(getLanguage(settingsDTO.language()));
            saveSettings(userSettings);
            return settingsDTOMapper.apply(userSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Do both of the following since the frontend sends the settings a strings

    private Language getLanguage(String language) {
        return switch (language.toLowerCase()) {
            case "en" -> Language.EN;
            case "de" -> Language.DE;
            default -> throw new IllegalArgumentException("Invalid language");
        };
    }

    private PageLayout getPageLayout(String pageLayout) {
        return switch (pageLayout.toLowerCase()) {
            case "cards" -> PageLayout.CARDS;
            case "list" -> PageLayout.LIST;
            default -> throw new IllegalArgumentException("Invalid page layout");
        };
    }
}
