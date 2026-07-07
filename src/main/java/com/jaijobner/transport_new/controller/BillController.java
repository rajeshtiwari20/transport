package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.bill.BillGetResp;
import com.jaijobner.transport_new.dto.bill.BillReq;
import com.jaijobner.transport_new.dto.bill.BillResp;
import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.service.BillService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<Page<BillResp>>> getBills(@Valid @RequestBody BillReq req) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Bills retrieved successfully", billService.getBills(req)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving bills"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBill(@Valid @RequestBody BillWriteReq req) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            billService.createBill(req);
            return ResponseEntity.ok(ApiResponse.success("Bill created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating the bill"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillGetResp>> getBill(@PathVariable Long id) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Bill retrieved successfully", billService.getBill(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail("Bill not found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the bill"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateBill(@PathVariable Long id, @Valid @RequestBody BillWriteReq req) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            billService.updateBill(id, req);
            return ResponseEntity.ok(ApiResponse.success("Bill updated successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail("Bill not found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the bill"));
        }
    }
}
