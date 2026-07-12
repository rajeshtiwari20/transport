package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.BillDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillDetailRepository extends JpaRepository<BillDetailEntity, Long> {
    Optional<BillDetailEntity> findByUnloadingId(Long unloadingId);
}
