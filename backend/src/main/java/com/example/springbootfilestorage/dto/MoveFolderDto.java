package com.example.springbootfilestorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoveFolderDto {
    @JsonProperty("containerId")
    private Long containerId;
    @JsonProperty("containerTargetId")
    private Long containerTargetId;
}
