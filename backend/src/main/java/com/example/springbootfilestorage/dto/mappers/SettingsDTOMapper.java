package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dto.settings.SettingsDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SettingsDTOMapper implements Function<Settings, SettingsDTO> {
    @Override
    public SettingsDTO apply(Settings settings) {
        return new SettingsDTO(
                // Using lowercase to make the enum values easier to read and since it's used in the frontend
                settings.getPageLayout().toString().toLowerCase(),
                settings.getLanguage().toString().toLowerCase()
        );
    }
}
