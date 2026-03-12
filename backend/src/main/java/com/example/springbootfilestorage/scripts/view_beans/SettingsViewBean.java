package com.example.springbootfilestorage.scripts.view_beans;

import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.service.SettingsService;
import org.springframework.stereotype.Component;

/**
 * The SettingsViewBean class serves as a component in the application
 * that provides view-related configuration data based on user settings.
 * It interacts with the SettingsService to retrieve user preferences and
 * determine the layout representation for page views.
 */
@Component
public class SettingsViewBean {

    private final SettingsService settingsService;

    public SettingsViewBean(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    //public boolean isPageLayoutCards() {
    //  return settingsService.getSettingsFromUser().getPageLayout().equals(PageLayout.CARDS);
    // }

    // public String getPageLayoutView() {return settingsService.getSettingsFromUser().getPageLayout().toString();}

    public void changePageLayoutView(String pageLayout) {
        // Settings userSettings = settingsService.getSettingsFromUser();
        Settings userSettings = null;
        switch (pageLayout) {
            case "CARDS":
                userSettings.setPageLayout(PageLayout.LIST);
                break;
            case "FOLDER":
                userSettings.setPageLayout(PageLayout.CARDS);
                break;
            default:
                throw new IllegalArgumentException("Invalid page layout: " + pageLayout);
        }
    }

}
