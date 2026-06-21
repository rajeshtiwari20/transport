package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.PartyEntity;
import com.jaijobner.transport_new.enums.PartyType;
import com.jaijobner.transport_new.repository.projection.PartyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyRepository extends JpaRepository<PartyEntity, Long> {
    Page<PartyEntity> findByPartyNameContainingIgnoreCaseOrMobileContainingIgnoreCase(String partyName, String mobile, Pageable pageable);

    List<PartyProjection> findByPartyTypeOrderByPartyNameAsc(PartyType partyName);
}
