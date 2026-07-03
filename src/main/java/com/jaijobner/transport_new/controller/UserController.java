package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.user.UserUpdateReq;
import com.jaijobner.transport_new.security.UserDetailsImpl;
import com.jaijobner.transport_new.service.UserService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser() {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        UserDetailsImpl userDetails = SecurityUtils.getCurrentUser();

        Map<String, Object> data = new HashMap<>();
        data.put("id", userDetails.getId());
        data.put("email", userDetails.getEmail());
        data.put("firstName", userDetails.getFirstName());
        data.put("lastName", userDetails.getLastName());

        return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", data));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateReq req) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        if (!SecurityUtils.getCurrentUserId().equals(userId)) {
            return ResponseEntity.status(403).body(ApiResponse.fail("Forbidden"));
        }

        try {
            userService.updateUser(userId, req);
            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("User not found")) {
                return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the profile"));
        }
    }
}
