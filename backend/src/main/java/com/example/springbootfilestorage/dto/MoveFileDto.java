package com.example.springbootfilestorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// Used tp drag a file into a folder
@Setter
@Getter
public class MoveFileDto {
    @JsonProperty("fileId")
    private Long fileId;
    @JsonProperty("containerId")
    private Long containerId;
}
