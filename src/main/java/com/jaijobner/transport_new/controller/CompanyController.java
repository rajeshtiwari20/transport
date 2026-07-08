package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.company.CompanyCompactResp;
import com.jaijobner.transport_new.dto.company.CompanyCreateReq;
import com.jaijobner.transport_new.dto.company.CompanyGetResp;
import com.jaijobner.transport_new.dto.company.CompanyUpdateReq;
import com.jaijobner.transport_new.entity.Company;
import com.jaijobner.transport_new.mapper.CompanyMapper;
import com.jaijobner.transport_new.service.CompanyService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyMapper companyMapper;

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
    public ResponseEntity<ApiResponse<CompanyGetResp>> getCompanyById(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            Company company = companyService.getCompanyById(id);
            if (company == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Company not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Company retrieved successfully", companyMapper.companyEntityToCompanyGetResp(company)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving company"));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Company>> createCompany(@Valid @RequestBody CompanyCreateReq createCompany) {
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyUpdateReq updateCompany) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try{
            Company companyEntity = companyService.getCompanyById(id);
            if (companyEntity == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Company not found"));
            }

            companyEntity.setCompanyCode(updateCompany.getCompanyCode());
            companyEntity.setCompanyName(updateCompany.getCompanyName());
            companyEntity.setContactPersonName(updateCompany.getContactPersonName());
            companyEntity.setContactPersonMobile(updateCompany.getContactPersonMobile());
            companyEntity.setCompanyType(updateCompany.getCompanyType());
            companyEntity.setEmail(updateCompany.getEmail());
            companyEntity.setMobile(updateCompany.getMobile());
            companyEntity.setAddress1(updateCompany.getAddress1());
            companyEntity.setAddress2(updateCompany.getAddress2());
            companyEntity.setDistrict(updateCompany.getDistrict());
            companyEntity.setState(updateCompany.getState());
            companyEntity.setPinNo(updateCompany.getPinNo());
            companyEntity.setGstNo(updateCompany.getGstNo());


            return ResponseEntity.ok(ApiResponse.success("Company updated successfully", companyService.updateCompany(companyEntity)))   ;
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating company"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try{
            Company companyEntity = companyService.getCompanyById(id);
            if (companyEntity == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Company not found"));
            }

            companyService.deleteCompany(companyEntity);
            return ResponseEntity.ok(ApiResponse.success("Company deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while deleting company"));
        }
    }

    @GetMapping("/compact/list")
    public ResponseEntity<ApiResponse<List<CompanyCompactResp>>> getCompactList() {
        if(!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try{
            return ResponseEntity.ok(ApiResponse.success("Compact company list retrieved successfully", companyService.getCompactList()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while getting compact company list"));
        }
    }

}
