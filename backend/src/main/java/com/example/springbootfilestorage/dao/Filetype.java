package com.example.springbootfilestorage.dao;

public enum Filetype {
    PDF,
    PNG,
    MP3;

    public String toString() {
        return super.toString().toUpperCase();
    }
}
