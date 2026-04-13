package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.systemsettings.CanRegisterDTO;
import com.example.springbootfilestorage.service.SystemSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system_settings")
public class SystemSettingsController {

    private final SystemSettingsService systemSettingsService;

    public SystemSettingsController(SystemSettingsService systemSettingsService) {
        this.systemSettingsService = systemSettingsService;
    }

    @GetMapping("/can_register")
    public ResponseEntity<CanRegisterDTO> canRegister() {
        return ResponseEntity.ok(systemSettingsService.canRegister());
    }
}
