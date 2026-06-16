package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.company.CompanyRequest;
import com.jaijobner.transport_new.entity.Company;
import com.jaijobner.transport_new.service.CompanyService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCompanies(){
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success("Companies retrieved successfully", Map.of("companies", companyService.getAllCompanies())));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving companies"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> getCompanyById(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            Company company = companyService.getCompanyById(id);
            if (company == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Company not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Company retrieved successfully", company));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving company"));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Company>> createCompany(@Valid @RequestBody CompanyRequest createCompany) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try{
            Company companyEntity = new Company();

            companyEntity.setCompanyCode(createCompany.getCompanyCode());
            companyEntity.setCompanyName(createCompany.getCompanyName());
            companyEntity.setContactPersonName(createCompany.getContactPersonName());
            companyEntity.setContactPersonMobile(createCompany.getContactPersonMobile());
            companyEntity.setCompanyType(createCompany.getCompanyType());
            companyEntity.setEmail(createCompany.getEmail());
            companyEntity.setMobile(createCompany.getMobile());
            companyEntity.setAddress1(createCompany.getAddress1());
            companyEntity.setAddress2(createCompany.getAddress2());
            companyEntity.setDistrict(createCompany.getDistrict());
            companyEntity.setState(createCompany.getState());
            companyEntity.setPinNo(createCompany.getPinNo());
            companyEntity.setGstNo(createCompany.getGstNo());


            return ResponseEntity.ok(ApiResponse.success("Company created successfully", companyService.createCompany(companyEntity)))   ;
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating company"));
        }
    }
}
