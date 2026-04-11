package com.example.springbootfilestorage.controller;

import com.example.springbootfilestorage.dto.search.SearchResultDTO;
import com.example.springbootfilestorage.dto.search.SearchTermDTO;
import com.example.springbootfilestorage.service.FileService;
import com.example.springbootfilestorage.service.FolderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final FolderService folderService;
    private final FileService fileService;

    public SearchController(FolderService folderService, FileService fileService) {
        this.folderService = folderService;
        this.fileService = fileService;
    }

    @GetMapping("")
    public ResponseEntity<SearchResultDTO> search(@RequestBody SearchTermDTO searchTermDTO) {
        try {
            return ResponseEntity.ok(
                    new SearchResultDTO(
                            folderService.searchFoldersByName(searchTermDTO),
                            fileService.searchFilesByName(searchTermDTO)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
