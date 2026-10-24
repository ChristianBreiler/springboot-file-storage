package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dto.settings.SettingsDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettingsDTOMapperTest {

    private final SettingsDTOMapper mapper = new SettingsDTOMapper();

    @Test
    void apply_lowercasesEnumValues() {
        Settings settings = new Settings();
        settings.setPageLayout(PageLayout.CARDS);
        settings.setLanguage(Language.DE);

        SettingsDTO result = mapper.apply(settings);

        assertThat(result).isEqualTo(new SettingsDTO("cards", "de"));
    }
}
