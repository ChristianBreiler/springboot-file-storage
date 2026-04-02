package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dto.settings.SettingsDTO;
import com.example.springbootfilestorage.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("")
    public ResponseEntity<SettingsDTO> show() {
        return ResponseEntity.ok(settingsService.getSettingsFromUser());
    }

    @PostMapping("/update")
    public ResponseEntity<SettingsDTO> update(@RequestBody SettingsDTO settingsDTO) {
        return ResponseEntity.ok(settingsService.updateSettings(settingsDTO));
    }
}
