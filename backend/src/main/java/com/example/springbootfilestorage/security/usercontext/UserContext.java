package com.example.springbootfilestorage.security.usercontext;

import com.example.springbootfilestorage.security.dao.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * UserContext is a Spring Component responsible for providing information
 * about the currently authenticated user.
 * <p>
 * This class interacts with Spring Security's `SecurityContextHolder`
 * to retrieve the authentication context and extract the user details.
 * It ensures that a user is authenticated and available before
 * returning the corresponding User object.
 * <p>
 * Throws a runtime exception if the user is not authenticated
 * or if the authenticated principal's user details are unavailable.
 */
@Component
public class UserContext {
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User is not authenticated");
        }

        try {
            return (User) auth.getPrincipal();
        } catch (ClassCastException e) {
            throw new RuntimeException("Principal is not a User object: " + auth.getPrincipal());
        }
    }
}
