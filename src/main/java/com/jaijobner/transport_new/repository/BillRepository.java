package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BillRepository extends JpaRepository<BillEntity, Long>, JpaSpecificationExecutor<BillEntity> {
}
