package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("")
    public ResponseEntity<Settings> show() {
        return ResponseEntity.ok(settingsService.getSettingsFromUser());
    }

    @PostMapping("/update")
    public ResponseEntity<Settings> update(@RequestParam String pageLayout, @RequestParam String language) {
        return ResponseEntity.ok(settingsService.updateSettings(pageLayout, language));
    }
}
