package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.repository.SettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    // public Settings getSettingsFromUser() {}

    // public int fileDeletionWeeks() { return getSettingsFromUser().getDeleteFilesAfterXWeeks();}

    public void saveSettings(Settings settings) {
        settingsRepository.save(settings);
    }

    public Settings updateSettings(String pageLayout, String language) {
        // Settings userSettings = getSettingsFromUser();
        Settings userSettings = null;
        userSettings.setPageLayout(pageLayout.equals("cards") ? PageLayout.CARDS : PageLayout.LIST);
        userSettings.setLanguage(language.equals("en") ? Language.EN : Language.DE);
        saveSettings(userSettings);
        return userSettings;
    }
}
