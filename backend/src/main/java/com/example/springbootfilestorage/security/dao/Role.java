package com.example.springbootfilestorage.security.dao;

public enum Role {
    USER,
    ADMIN;

    @Override
    public String toString() {
        return switch (this) {
            case USER -> "USER";
            case ADMIN -> "ADMIN";
            default -> "";
        };
    }
}

