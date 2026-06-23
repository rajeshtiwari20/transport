package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.UnloadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UnloadingRepository extends JpaRepository<UnloadingEntity, Long>, JpaSpecificationExecutor<UnloadingEntity> {
}
