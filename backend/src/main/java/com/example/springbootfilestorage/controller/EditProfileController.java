package com.example.springbootfilestorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/edit-profile")
public class EditProfileController {

    @PostMapping("")
    public String editProfile() {

        return "edit-profile";
    }
}
