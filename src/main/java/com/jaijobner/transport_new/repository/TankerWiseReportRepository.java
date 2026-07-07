package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.report.TankerWiseReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TankerWiseReportRepository extends JpaRepository<TankerWiseReportEntity, Long> {
    List<TankerWiseReportEntity> findByLrDateBetween(LocalDate startDate, LocalDate endDate);

    Page<TankerWiseReportEntity> findByLrDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<TankerWiseReportEntity> findByLrDateBetweenAndTruckNumber(LocalDate startDate, LocalDate endDate, String truckNumber);

    Page<TankerWiseReportEntity> findByLrDateBetweenAndTruckNumber(LocalDate startDate, LocalDate endDate, String truckNumber, Pageable pageable);
}
