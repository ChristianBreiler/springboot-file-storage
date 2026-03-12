package com.example.springbootfilestorage.dao;

public enum PageLayout {
    CARDS,
    LIST;

    public String toString() {
        return super.toString().toLowerCase();
    }
}
