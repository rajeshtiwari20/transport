package com.jaijobner.transport_new.utils;

import com.jaijobner.transport_new.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class SecurityUtils {
    public static UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        }
        throw new RuntimeException("Principal is not of type UserDetailsImpl");
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    // Returns only the user ID
    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    // Returns the user's email
    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }
}
