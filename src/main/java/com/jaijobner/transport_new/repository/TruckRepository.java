package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.TruckEntity;
import com.jaijobner.transport_new.repository.projection.TruckProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TruckRepository extends JpaRepository<TruckEntity, Long>, JpaSpecificationExecutor<TruckEntity> {
    List<TruckProjection> findAllProjectedByOrderByTruckNumAsc();
}
