package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    // @GetMapping("")
    //public ResponseEntity<Settings> show() {return new ResponseEntity<Settings>(settingsService.getSettingsFromUser(), HttpStatus.OK);}

    @PostMapping("/update")
    public ResponseEntity<Settings> update(@RequestParam String pageLayout, @RequestParam String language) {
        Settings updatedSettings = settingsService.updateSettings(pageLayout, language);
        return ResponseEntity.ok(updatedSettings);
    }
}
