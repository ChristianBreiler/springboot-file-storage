package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dao.UploadedFile;
import com.example.springbootfilestorage.dao.Filetype;
import com.example.springbootfilestorage.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SharedFilesController {

    private final FileService fileService;

    public SharedFilesController(FileService fileService) {
        this.fileService = fileService;
    }

    // TODO: Can stil ldelete files this way
    @GetMapping("/shared_file/{file_share_code}")
    public String showSharedFile(@PathVariable String file_share_code, Model model) {
        UploadedFile sharedFile = fileService.getFileByFileShareCode(file_share_code);
        if (sharedFile == null) {
            // TODO: Message
            return "redirect:/home";
        }
        // TODO: Add access privileges
        model.addAttribute("sharedFile", sharedFile);
        model.addAttribute("file_owner", sharedFile.getOwner());
        model.addAttribute("Filetype", Filetype.class);
        return "shared_file";
    }
}
