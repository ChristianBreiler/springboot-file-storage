package com.example.springbootfilestorage.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
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
