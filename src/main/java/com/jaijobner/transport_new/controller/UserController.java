package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.security.UserDetailsImpl;
import com.jaijobner.transport_new.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
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
}
