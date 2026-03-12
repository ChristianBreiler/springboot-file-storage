package com.example.springbootfilestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringbootFileStorageApplication serves as the entry point for the Spring Boot application.
 * It bootstraps the application context and starts the embedded server.
 * <p>
 * This application provides functionality for file storage management, and it integrates
 * various backend services including user management, role-based security, and settings
 * configuration. It prepares the application context for dependency injection and
 * ensures the application is ready to handle client requests.
 * <p>
 * Responsibilities:
 * - Initializes the application context.
 * - Configures core application beans and components.
 * - Starts the embedded server to handle API requests.
 * <p>
 * The application is designed to include functionalities like user authentication,
 * role management, and customizable settings for file management.
 */

@SpringBootApplication
public class SpringbootFileStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootFileStorageApplication.class, args);
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) for the application, allowing
     * all origins, headers, and methods while disabling credential sharing and
     * setting a maximum age for preflight requests.
     *
     * @return a {@code WebMvcConfigurer} bean that customizes the application's CORS policy
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }

}
