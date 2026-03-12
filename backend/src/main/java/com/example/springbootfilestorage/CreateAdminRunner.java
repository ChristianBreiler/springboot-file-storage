package com.example.springbootfilestorage;

import com.example.springbootfilestorage.dao.Language;
import com.example.springbootfilestorage.dao.PageLayout;
import com.example.springbootfilestorage.dao.Settings;
import com.example.springbootfilestorage.repository.SettingsRepository;
import com.example.springbootfilestorage.security.user.Role;
import com.example.springbootfilestorage.security.user.User;
import com.example.springbootfilestorage.security.user.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

/**
 * The CreateAdminRunner class is a Spring Boot application used to create an admin user
 * in the system. It initializes the application context, retrieves required beans, and
 * ensures that an admin user exists in the database. If an admin user is not present,
 * it creates one with default credentials.
 * <p>
 * This application is designed to run once and terminate after creating the admin user,
 * if necessary.
 * <p>
 * Responsibilities:
 * - Start the Spring Boot application context.
 * - Retrieve the UserRepository and PasswordEncoder from the context.
 * - Check if an admin user exists in the database.
 * - Create and persist an admin user with pre-configured values if none exists.
 * - Exit the application after the process completes.
 */
@SpringBootApplication
public class CreateAdminRunner {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CreateAdminRunner.class, args);

        UserRepository userRepository = context.getBean(UserRepository.class);
        SettingsRepository settingsRepository = context.getBean(SettingsRepository.class);
        // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        createAdmin(userRepository, settingsRepository);

        System.exit(0);
    }

    /**
     * Creates an admin user with default settings if it does not already exist in the database.
     * The method checks for the existence of an admin user by its username, and if it does not exist,
     * a new user with administrator privileges is created along with associated settings and persisted.
     * <p>
     * username: admin
     * password: Admin123
     *
     * @param userRepository     the repository to manage User entities
     * @param settingsRepository the repository to manage Settings entities
     */
    private static void createAdmin(UserRepository userRepository, SettingsRepository settingsRepository) {
        String username = "admin";
        if (userRepository.findByUsername(username).isEmpty()) {
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("User");
            admin.setUsername("admin");
            admin.setEmailaddress("admin@example.com");
            admin.setPassword("Admin123");
            admin.setRole(Role.ADMIN);
            admin.setCreatedAt(LocalDate.now());

            Settings settings = new Settings();
            settings.setPageLayout(PageLayout.CARDS);
            settings.setLanguage(Language.EN);
            settings.setDeleteFilesAfterXWeeks(2);
            settingsRepository.save(settings);

            admin.setSettings(settings);
            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}