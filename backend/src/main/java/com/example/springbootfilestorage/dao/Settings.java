package com.example.springbootfilestorage.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "settings")
public class Settings extends BaseDAO {

    @Column(nullable = false)
    private PageLayout pageLayout;

    @Column(nullable = false)
    private Language language;

    @Column(nullable = false)
    private int deleteFilesAfterXWeeks;
}