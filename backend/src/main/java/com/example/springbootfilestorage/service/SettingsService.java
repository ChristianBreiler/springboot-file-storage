package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.repository.SettingsRepository;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final UserContext userContext;

    public SettingsService(SettingsRepository settingsRepository, UserContext userContext) {
        this.settingsRepository = settingsRepository;
        this.userContext = userContext;
    }

    public Settings getSettingsFromUser() {
        return userContext.getAuthenticatedUser().getSettings();
    }

    public int fileDeletionWeeks() {
        return getSettingsFromUser().getDeleteFilesAfterXWeeks();
    }

    public void saveSettings(Settings settings) {
        settingsRepository.save(settings);
    }

    public Settings updateSettings(String pageLayout, String language) {
        Settings userSettings = getSettingsFromUser();
        userSettings.setPageLayout(pageLayout.equals("cards") ? PageLayout.CARDS : PageLayout.LIST);
        userSettings.setLanguage(language.equals("en") ? Language.EN : Language.DE);
        saveSettings(userSettings);
        return userSettings;
    }
}
