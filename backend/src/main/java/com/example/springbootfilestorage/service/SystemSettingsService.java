package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dto.systemsettings.CanRegisterDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsService {
    @Value("${spring.registration.can.register}")
    private boolean canRegister;

    public CanRegisterDTO canRegister() {
        return new CanRegisterDTO(canRegister);
    }
}
