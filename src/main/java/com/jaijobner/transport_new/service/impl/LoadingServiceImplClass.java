package com.jaijobner.transport_new.service.impl;

import com.jaijobner.transport_new.dto.loading.LoadingCreateReq;
import com.jaijobner.transport_new.dto.loading.LoadingGetResp;
import com.jaijobner.transport_new.dto.loading.LoadingReq;
import com.jaijobner.transport_new.dto.loading.LoadingResp;
import com.jaijobner.transport_new.entity.*;
import com.jaijobner.transport_new.mapper.LoadingDetailsMapper;
import com.jaijobner.transport_new.mapper.LoadingMapper;
import com.jaijobner.transport_new.mapper.LoadingMaterialMapper;
import com.jaijobner.transport_new.repository.*;
import com.jaijobner.transport_new.service.LoadingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoadingServiceImplClass implements LoadingService {
    @Autowired
    LoadingRepository loadingRepository;
    private final LoadingMaterialRepository loadingMaterialRepository;
    private final LoadingDetailsRepository loadingDetailsRepository;
    private final CompanyRepository companyRepository;
    private final PartyRepository partyRepository;
    private final DriverRepository driverRepository;
    private final TruckRepository truckRepository;


    private final LoadingMapper loadingMapper;
    private final LoadingDetailsMapper loadingDetailsMapper;
    private final LoadingMaterialMapper loadingMaterialMapper;

    @Override
    public Page<LoadingResp> getLoadings(LoadingReq req) {
        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo()-1, req.getPageSize(), sort);

        Specification<LoadingEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getSearchTerm() != null && !req.getSearchTerm().trim().isEmpty()) {
                String pattern = "%" + req.getSearchTerm().trim().toLowerCase() + "%";
                Predicate companyName = cb.like(cb.lower(root.get("companyName")), pattern);
                Predicate lrNumber = cb.like(cb.lower(root.get("lrNumber")), pattern);
                Predicate truckNumber = cb.like(cb.lower(root.get("truckNumber")), pattern);
                Predicate consigneeName = cb.like(cb.lower(root.get("consigneeName")), pattern);
                Predicate consignorName = cb.like(cb.lower(root.get("consignorName")), pattern);
                predicates.add(cb.or(companyName, lrNumber, truckNumber, consigneeName, consignorName));
            }

            if (req.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), req.getStartDate()));
            }
            if (req.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), req.getEndDate()));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };

        return loadingRepository.findAll(spec, pageable).map(loadingMapper::toLoadingRespFromLoadingEntity);
    }

    @Override
    @Transactional
    public void createLoading(LoadingCreateReq req) {
        try {
            Company company = companyRepository.findById(req.getCompanyId()).orElseThrow(() -> new EntityNotFoundException("Company not found with id:" + req.getCompanyId()));

            TruckEntity truck = truckRepository.findById(req.getTruckId()).orElseThrow(() -> new EntityNotFoundException("Truck not found with id:" + req.getTruckId()));

            PartyEntity consignee = partyRepository.findById(req.getConsigneeId()).orElseThrow(() -> new EntityNotFoundException("Consignee not found with id:" + req.getConsigneeId()));

            PartyEntity consignor = partyRepository.findById(req.getConsignorId()).orElseThrow(() -> new EntityNotFoundException("Consignor not found with id:" + req.getConsignorId()));

            DriverEntity driver1 = null;
            if (Objects.nonNull(req.getDriver1())) {
                driver1 = driverRepository.findById(req.getDriver1()).orElseThrow(() -> new EntityNotFoundException("Driver 1 not found with id:" + req.getDriver1()));
            }

            DriverEntity driver2 = null;
            if (Objects.nonNull(req.getDriver2())) {
                driver2 = driverRepository.findById(req.getDriver2()).orElseThrow(() -> new EntityNotFoundException("Driver 2 not found with id:" + req.getDriver2()));
            }

            LoadingEntity loadingEntity = loadingMapper.toLoadingEntityFromLoadingCreateReq(req, company, truck, consignee, consignor);
            String lrNumber = generateLrNumber(req);
            loadingEntity.setLrNumber(lrNumber);

            //save
            LoadingEntity savedLoading = loadingRepository.save(loadingEntity);
            log.info("Loading created with ID: {}", savedLoading.getId());

            LoadingDetailsEntity loadingDetailsEntity = loadingDetailsMapper.toLoadingDetailsEntity(req, driver1, driver2);
            loadingDetailsEntity.setLoading(savedLoading);
            loadingDetailsRepository.save(loadingDetailsEntity);

            List<LoadingMaterialEntity> materials = req.getLoadingMaterialReqList().stream()
                    .map(loadingMaterialMapper::toLoadingMaterialEntity)
                    .peek(mat -> mat.setLoading(savedLoading))
                    .toList();
            loadingMaterialRepository.saveAll(materials);

            log.info("Loading creation completed for LR: {}", lrNumber);
        } catch (Exception e) {
            log.error("Error occurred while creating loading: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create loading: " + e.getMessage());
        }

    }

    @Override
    public LoadingGetResp getLoading(Long id) {
        LoadingEntity loadingEntity =  loadingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Loading not found with id:" + id));
        log.info("Loading found with ID: {}, LR Number: {}", id, loadingEntity.getLrNumber());
        return loadingMapper.toLoadingGetRespFromLoadingEntity(loadingEntity);
    }

    private String generateLrNumber(LoadingCreateReq req) {
        Optional<LoadingEntity> lastLoadingEntry = loadingRepository.findFirstByCompanyIdOrderByIdDesc(req.getCompanyId());
        if(lastLoadingEntry.isPresent()) {
            LoadingEntity loadingEntity = lastLoadingEntry.get();
            long lastLrNumber = Long.parseLong(loadingEntity.getLrNumber());
            return String.valueOf(lastLrNumber + 1);
        } else {
            return "1";
        }
    }
}
