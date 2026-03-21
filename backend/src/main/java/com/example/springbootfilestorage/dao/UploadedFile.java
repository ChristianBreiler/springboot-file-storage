package com.example.springbootfilestorage.dao;

import com.example.springbootfilestorage.security.dao.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
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

    private boolean isProfilePic;

    // Gets used in the front end to display the file image
    @Transient
    private String fileImage;

    // Used to share files with other users
    @Column(name = "file_share_code", unique = true, nullable = false)
    private String fileShareCode;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    // Date at which the file will be deleted from the database and server
    @Column(name = "final_deletion_date", nullable = true)
    private LocalDate finalDeletionDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    public UploadedFile(String originalFilename, String storagePath, Long size, Folder folder,
                        Filetype filetype, String storedName) {
        this.originalFilename = originalFilename;
        this.storagePath = storagePath;
        this.size = size;
        this.folder = folder;
        this.filetype = filetype;
        this.storedName = storedName;
    }

    public String probeContentType() {
        return switch (this.filetype) {
            case PDF -> "application/pdf";
            case PNG -> "image/png";
            // Fallback
            default -> "application/octet-stream";
        };
    }
}
