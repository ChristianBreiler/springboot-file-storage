package com.example.springbootfilestorage.security.model;

import com.example.springbootfilestorage.dao.BaseDAO;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dao.UploadedFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseDAO implements UserDetails {

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    // TODO: Maybe remove username?
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String emailaddress;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @Column(name = "verification_code", nullable = true)
    private String verificationCode;

    @Column(name = "verification_code_expiry", nullable = true)
    private LocalDateTime verificationCodeExpiry;

    @ManyToOne
    @JoinColumn(name = "profile_pic_id")
    private UploadedFile profilePic;

    private Role role;

    @OneToOne
    @JoinColumn(name = "settings_id")
    private Settings settings;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        // return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        // return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        // return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
