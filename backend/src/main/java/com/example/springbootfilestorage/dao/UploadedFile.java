package com.example.springbootfilestorage.dao;

import com.example.springbootfilestorage.security.dao.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "files")
public class UploadedFile extends BaseDAO {

    @Column(nullable = false, unique = false)
    private String originalFilename;
    private String storedName;
    private Long size;
    private String storagePath;
    private Filetype filetype;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    // Gets used in the front end to display the file image
    @Transient
    private String fileImage;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    public String probeContentType() {
        return switch (this.filetype) {
            case PDF -> "application/pdf";
            case PNG -> "image/png";
            // Fallback
            default -> "application/octet-stream";
        };
    }
}
