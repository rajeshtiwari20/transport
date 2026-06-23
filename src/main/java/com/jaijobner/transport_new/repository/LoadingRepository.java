package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.enums.LoadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LoadingRepository extends JpaRepository<LoadingEntity, Long>, JpaSpecificationExecutor<LoadingEntity> {
    Optional<LoadingEntity> findFirstByCompanyIdOrderByIdDesc(Long companyId);

    List<LoadingEntity> findByStatusOrderByIdDesc(LoadingStatus status);
}
