package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.driver.*;
import com.jaijobner.transport_new.mapper.DriverMapper;
import com.jaijobner.transport_new.service.DriverService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverMapper driverMapper;

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<Page<DriverResp>>> getDrivers(@Valid @RequestBody DriverReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }


        try {
            var drivers = driverService.getAllDrivers(req).map(driverMapper::driverEntityToDriverResp);
            return ResponseEntity.ok(ApiResponse.success("Drivers retrieved successfully", drivers));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving drivers"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createDriver(@Valid @RequestBody DriverCreateReq req){
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            driverService.createDriver(req);
            return ResponseEntity.ok(ApiResponse.success("Driver created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating the driver"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResp>> getDriver(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            DriverResp truck = driverService.getDriverById(id)
                    .map(driverMapper::driverEntityToDriverResp)
                    .orElse(null);
            if (truck == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Driver not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Driver retrieved successfully", truck));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the driver"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateDriver(@PathVariable Long id, @Valid @RequestBody DriverUpdateReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            driverService.updateDriver(id, req);
            return ResponseEntity.ok(ApiResponse.success("Driver updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the driver"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTruck(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            driverService.deleteDriver(id);
            return ResponseEntity.ok(ApiResponse.success("Driver deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while deleting the driver"));
        }
    }

    @GetMapping("/compact/list")
    public ResponseEntity<ApiResponse<List<DriverCompactResp>>> getCompactList() {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Driver compact list retrieved successfully", driverService.getCompactList()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the driver compact list"));
        }
    }
}
