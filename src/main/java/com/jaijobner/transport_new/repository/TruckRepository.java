package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.TruckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TruckRepository extends JpaRepository<TruckEntity, Long>, JpaSpecificationExecutor<TruckEntity> {
}
