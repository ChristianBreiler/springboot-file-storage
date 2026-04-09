package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.profile.EditProfileDTO;
import com.example.springbootfilestorage.dto.profile.ProfileDTO;
import com.example.springbootfilestorage.security.service.UserService;
import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/profile")
@RestController
public class ProfileController {

    private final FileService fileService;
    private final int MAX_MESSAGES = 10;
    private final MessageService messageService;
    private final UserService userService;

    public ProfileController(FileService fileService, MessageService messageService, UserService userService) {
        this.fileService = fileService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<ProfileDTO> show() {
        ProfileDTO profile = userService.getProfile();
        if (profile == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/edit")
    public ResponseEntity<ProfileDTO> edit(@RequestBody EditProfileDTO editedProfile) {
        return ResponseEntity.ok(userService.editProfile(editedProfile));
    }

    @PostMapping("/uploadProfilePic")
    public ResponseEntity<?> uploadProfilePic(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || fileTooBig(file)) {
            return ResponseEntity.badRequest().body("File too big or empty.");
        }
        fileService.saveProfilePic(file);
        return ResponseEntity.ok().build();
    }

    private boolean fileTooBig(MultipartFile file) {
        return file.getSize() > 1073741824;
    }
}
