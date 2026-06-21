package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.loading.*;
import com.jaijobner.transport_new.service.LoadingService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loading")
public class LoadingController {
    @Autowired
    private LoadingService loadingService;

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<Page<LoadingResp>>> getLoadings(@Valid @RequestBody LoadingReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            Page<LoadingResp> loadings = loadingService.getLoadings(req);
            return ResponseEntity.ok(ApiResponse.success("Loadings retrieved successfully", loadings));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving loadings"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createLoading(@Valid @RequestBody LoadingCreateReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            loadingService.createLoading(req);
            return ResponseEntity.ok(ApiResponse.success("Loading created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating the loading"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoadingGetResp>> getLoading(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            LoadingGetResp loading = loadingService.getLoading(id);
            if (loading == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Loading not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Loading retrieved successfully", loading));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the loading"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateLoading(@PathVariable Long id, @Valid @RequestBody LoadingUpdateReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            loadingService.updateLoading(id, req);
            return ResponseEntity.ok(ApiResponse.success("Loading retrieved successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the loading"));
        }
    }

    @GetMapping("lr-number/{companyId}")
    public ResponseEntity<ApiResponse<LoadingLRNumResp>> getNewLrNum(@PathVariable Long companyId) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Loading retrieved successfully", loadingService.getNewLrNum(companyId)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the loading"));
        }
    }
}
