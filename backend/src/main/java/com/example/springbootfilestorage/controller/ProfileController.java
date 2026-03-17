package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.ProfileDTO;
import com.example.springbootfilestorage.security.model.User;
import com.example.springbootfilestorage.security.service.UserService;
import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final FileService fileService;
    private final int MAX_MESSAGES = 10;
    private final MessageService messageService;
    private final UserService userService;

    public ProfileController(FileService fileService,
                             MessageService messageService, UserService userService) {
        this.fileService = fileService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<ProfileDTO> show() {
        ProfileDTO profile = userService.getProfile();
        if (profile == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<ProfileDTO>(profile, HttpStatus.OK);
    }

    @PostMapping("/change_username")
    public ResponseEntity<?> changeUsername(@RequestParam String username) {
        if (username.trim().isEmpty() || username.length() > 20) {
            return ResponseEntity.badRequest().body("Invalid username: Must be 1-20 characters.");
        }

        // User user = userService.changeUserName(username);
        return ResponseEntity.ok(new User());
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
