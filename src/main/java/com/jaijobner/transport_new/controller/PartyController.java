package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.party.*;
import com.jaijobner.transport_new.mapper.PartyMapper;
import com.jaijobner.transport_new.service.PartyService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/party")
public class PartyController {
    @Autowired
    PartyService partyService;

    @Autowired
    private PartyMapper partyMapper;

    @PostMapping("/parties")
    public ResponseEntity<ApiResponse<Page<PartyResp>>> getParties(@Valid @RequestBody PartyReq req){
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            Page<PartyResp> parties = partyService.getAllParties(req).map(partyMapper::partyEntityToPartyResp);
            return ResponseEntity.ok(ApiResponse.success("Parties retrieved successfully",parties));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving parties"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createParty(@Valid @RequestBody PartyCreateReq req){
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            partyService.createParty(req);
            return ResponseEntity.ok(ApiResponse.success("Party created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating the party"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PartyGetResp>> getParty(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            PartyGetResp party = partyService.getPartyById(id)
                    .map(partyMapper::partyEntityToPartyGetResp)
                    .orElse(null);
            if (party == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Party not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Party retrieved successfully", party));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the party"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateParty(@PathVariable Long id, @Valid @RequestBody PartyUpdateReq req) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            partyService.updateParty(id, req);
            return ResponseEntity.ok(ApiResponse.success("Party updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the party"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteParty(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            partyService.deleteParty(id);
            return ResponseEntity.ok(ApiResponse.success("Party deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while deleting the party"));
        }
    }
}
