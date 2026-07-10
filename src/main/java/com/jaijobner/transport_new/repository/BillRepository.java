package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.BillEntity;
import com.jaijobner.transport_new.entity.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Month;
import java.time.Year;
import java.util.Optional;

public interface BillRepository extends JpaRepository<BillEntity, Long>, JpaSpecificationExecutor<BillEntity> {
    Optional<BillEntity> findByConsigneeAndYearAndMonth(PartyEntity consignee, Year year, Month month);
}
