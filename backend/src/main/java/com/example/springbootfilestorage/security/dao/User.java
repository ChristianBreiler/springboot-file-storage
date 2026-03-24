package com.example.springbootfilestorage.security.dao;

import com.example.springbootfilestorage.dao.BaseDAO;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.dao.UploadedFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseDAO implements UserDetails {

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String emailaddress;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expiry")
    private LocalDateTime verificationCodeExpiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_pic_id")
    private UploadedFile profilePic;

    @Enumerated
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settings_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Settings settings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
