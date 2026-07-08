package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.report.TankerWiseReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TankerWiseReportRepository extends JpaRepository<TankerWiseReportEntity, Long> {
    List<TankerWiseReportEntity> findByUnloadingDateBetween(LocalDate startDate, LocalDate endDate);

    Page<TankerWiseReportEntity> findByUnloadingDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<TankerWiseReportEntity> findByUnloadingDateBetweenAndTruckNumberContaining(LocalDate startDate, LocalDate endDate, String truckNumber);

    Page<TankerWiseReportEntity> findByUnloadingDateBetweenAndTruckNumberContaining(LocalDate startDate, LocalDate endDate, String truckNumber, Pageable pageable);

    List<TankerWiseReportEntity> findByUnloadingDateBetweenAndConsignorNameContaining(LocalDate startDate, LocalDate endDate, String consignorName);

    Page<TankerWiseReportEntity> findByUnloadingDateBetweenAndConsignorNameContaining(LocalDate startDate, LocalDate endDate, String consignorName, Pageable pageable);
}
