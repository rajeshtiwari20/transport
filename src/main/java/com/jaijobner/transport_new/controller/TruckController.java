package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.truck.*;
import com.jaijobner.transport_new.mapper.TruckMapper;
import com.jaijobner.transport_new.service.TruckService;
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
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/truck")
public class TruckController {
    @Autowired
    private TruckService truckService;

    @Autowired
    private TruckMapper truckMapper;

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<Page<TruckResp>>> getTrucks(@Valid @RequestBody TruckReq req) {
        try {
            var trucks = truckService.getAllTrucks(req).map(truckMapper::truckEntityToTruckResp);
            return ResponseEntity.ok(ApiResponse.success("Trucks retrieved successfully", trucks));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving trucks"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createTruck(@Valid @RequestBody TruckCreateReq req){
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            truckService.createTruck(req);
            return ResponseEntity.ok(ApiResponse.success("Truck created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating the truck"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TruckResp>> getTruck(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            TruckResp truck = truckService.getTruckById(id)
                    .map(truckMapper::truckEntityToTruckResp)
                    .orElse(null);
            if (truck == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Truck not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Truck retrieved successfully", truck));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the truck"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTruck(@PathVariable Long id, @Valid @RequestBody TruckUpdateReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            truckService.updateTruck(id, req);
            return ResponseEntity.ok(ApiResponse.success("Truck updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the truck"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTruck(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            truckService.deleteTruck(id);
            return ResponseEntity.ok(ApiResponse.success("Truck deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while deleting the truck"));
        }
    }

    @GetMapping("/compact/list")
    public ResponseEntity<ApiResponse<List<TruckCompactResp>>> getCompactList(){
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Truck compact list fetched successfully", truckService.getCompactList()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while deleting the truck"));
        }
    }
}
