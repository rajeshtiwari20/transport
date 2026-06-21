package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.Company;
import com.jaijobner.transport_new.repository.projection.CompanyProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<CompanyProjection> findAllProjectedByOrderByCompanyNameAsc();
}
