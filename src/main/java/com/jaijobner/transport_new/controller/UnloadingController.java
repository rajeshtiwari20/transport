package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.unloading.UnloadingGetResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingReq;
import com.jaijobner.transport_new.dto.unloading.UnloadingResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingWriteReq;
import com.jaijobner.transport_new.service.UnloadingService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/unloading")
public class UnloadingController {
    @Autowired
    private UnloadingService unloadingService;

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<Page<UnloadingResp>>> getLoadings(@Valid @RequestBody UnloadingReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Unloadings retrieved successfully", unloadingService.getUnloadings(req)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving Unloadings"));
        }
    }

    @PostMapping("/nonbilled-list")
    public ResponseEntity<ApiResponse<Page<UnloadingResp>>> getNonBilledUnloadings(@Valid @RequestBody UnloadingReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Non-billed unloadings retrieved successfully", unloadingService.getNonBilledUnloadings(req)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving non-billed unloadings"));
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createUnloading(@Valid @RequestBody UnloadingWriteReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            unloadingService.createUnloading(req);
            return ResponseEntity.ok(ApiResponse.success("Unloading created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating the unloading"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnloadingGetResp>> getUnloading(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            UnloadingGetResp unloading = unloadingService.getUnloading(id);
            if (unloading == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Unloading not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Unloading retrieved successfully", unloading));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the unloading"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUnloading(@PathVariable Long id, @Valid @RequestBody UnloadingWriteReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            unloadingService.updateUnloading(id, req);
            return ResponseEntity.ok(ApiResponse.success("Unloading updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the unloading"));
        }
    }
}
