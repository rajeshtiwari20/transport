package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
