package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.DriverEntity;
import com.jaijobner.transport_new.repository.projection.DriverProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DriverRepository extends JpaRepository<DriverEntity, Long>, JpaSpecificationExecutor<DriverEntity> {
    List<DriverProjection> findAllProjectedByOrderByDriverNameAsc();
}
