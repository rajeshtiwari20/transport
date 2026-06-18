package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DriverRepository extends JpaRepository<DriverEntity, Long>, JpaSpecificationExecutor<DriverEntity> {
}
