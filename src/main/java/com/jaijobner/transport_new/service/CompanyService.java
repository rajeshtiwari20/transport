package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.company.CompanyCompactResp;
import com.jaijobner.transport_new.entity.Company;
import com.jaijobner.transport_new.mapper.CompanyMapper;
import com.jaijobner.transport_new.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyMapper companyMapper;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public void deleteCompany(Company company) {
        companyRepository.delete(company);
    }

    public List<CompanyCompactResp> getCompactList(){
        return companyMapper.toCompactRespList(companyRepository.findAllProjectedByOrderByCompanyNameAsc());
    }
}
