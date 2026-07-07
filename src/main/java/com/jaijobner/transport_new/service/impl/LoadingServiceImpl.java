package com.jaijobner.transport_new.service.impl;

import com.jaijobner.transport_new.dto.loading.*;
import com.jaijobner.transport_new.entity.TruckEntity;
import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.entity.Company;
import com.jaijobner.transport_new.entity.PartyEntity;
import com.jaijobner.transport_new.entity.DriverEntity;
import com.jaijobner.transport_new.entity.LoadingDetailsEntity;
import com.jaijobner.transport_new.entity.LoadingMaterialEntity;
import com.jaijobner.transport_new.enums.LoadingStatus;
import com.jaijobner.transport_new.mapper.LoadingDetailsMapper;
import com.jaijobner.transport_new.mapper.LoadingMapper;
import com.jaijobner.transport_new.mapper.LoadingMaterialMapper;
import com.jaijobner.transport_new.repository.LoadingRepository;
import com.jaijobner.transport_new.repository.LoadingDetailsRepository;
import com.jaijobner.transport_new.repository.PartyRepository;
import com.jaijobner.transport_new.repository.TruckRepository;
import com.jaijobner.transport_new.repository.LoadingMaterialRepository;
import com.jaijobner.transport_new.repository.CompanyRepository;
import com.jaijobner.transport_new.repository.DriverRepository;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoadingServiceImpl implements LoadingService {
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
                predicates.add(cb.greaterThanOrEqualTo(root.get("lrDate"), req.getStartDate()));
            }
            if (req.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("lrDate"), req.getEndDate()));
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
            String lrNumber = generateLrNumber(req.getCompanyId());
            loadingEntity.setLrNumber(lrNumber);
            loadingEntity.setCompanyName(company.getCompanyName());
            loadingEntity.setTruckNumber(truck.getTruckNum());
            loadingEntity.setConsigneeName(consignee.getPartyName());
            loadingEntity.setConsignorName(consignor.getPartyName());

            //save
            LoadingEntity savedLoading = loadingRepository.save(loadingEntity);
            log.info("Loading created with ID: {}", savedLoading.getId());

            LoadingDetailsEntity loadingDetailsEntity = loadingDetailsMapper.toLoadingDetailsEntity(req, driver1, driver2);
            loadingDetailsEntity.setLoading(savedLoading);
            loadingDetailsRepository.save(loadingDetailsEntity);

            LoadingMaterialEntity material = loadingMaterialMapper.toLoadingMaterialEntity(req.getLoadingMaterialReq());
            material.setLoading(savedLoading);
            loadingMaterialRepository.save(material);

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

    @Override
    @Transactional
    public void updateLoading(Long id, LoadingUpdateReq req) {
        LoadingEntity loadingEntity =  loadingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Loading not found with id:" + id));

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
        //saving loading entity
        loadingMapper.toLoadingEntityFromLoadingUpdateReq(req, company, truck, consignee, consignor, loadingEntity);
        loadingRepository.save(loadingEntity);

        //saving loading details entity
        LoadingDetailsEntity loadingDetailsEntity = loadingEntity.getLoadingDetails();
        loadingDetailsMapper.toLoadingDetailsEntityFromLoadingUpdateReq(req, driver1, driver2, loadingDetailsEntity);
        loadingDetailsRepository.save(loadingDetailsEntity);

        LoadingMaterialEntity material = loadingEntity.getLoadingMaterial();
        loadingMaterialMapper.toLoadingMaterialEntityFromLoadingUpdateReq(req.getLoadingMaterialReq(), material);
        material.setLoading(loadingEntity);
        loadingMaterialRepository.save(material);

    }

    @Override
    public LoadingLRNumResp getNewLrNum(Long companyId) {
        return loadingMapper.toLoadingLRNumResp(generateLrNumber(companyId));
    }

    @Override
    public List<LoadingUnloadingResp> getUploadingList() {
        return loadingMapper.toLoadingUnloadingRespFromLoadingEntity(loadingRepository.findByStatusOrderByIdDesc(LoadingStatus.UNLOADING));
    }

    @Override
    public LoadingMaterialWeightResp getLoadingMaterialWeight(Long id) {
        Double totalWeight = 0.0;
        String unit = "";

        Optional<LoadingEntity> loading = loadingRepository.findById(id);

        if (loading.isPresent()) {
            LoadingMaterialEntity material = loading.get().getLoadingMaterial();
            if (Objects.nonNull(material)) {
                totalWeight = material.getLoadedWeight();
                unit = material.getMaterialUnit();
            }
        }
        return loadingMapper.toLoadingMaterialWeightResp(totalWeight, unit);
    }

    @Override
    public LoadingUnloadingResp getLoadingByUnloadingId(Long unloadingId) {
        LoadingEntity loading = loadingRepository.findByUnloadingId(unloadingId);
        return loadingMapper.toLoadingUnloadingResp(loading);
    }

    private String generateLrNumber(Long companyId) {
        Optional<LoadingEntity> lastLoadingEntry = loadingRepository.findFirstByCompanyIdOrderByIdDesc(companyId);
        if(lastLoadingEntry.isPresent()) {
            LoadingEntity loadingEntity = lastLoadingEntry.get();
            long newLrNumber = Long.parseLong(loadingEntity.getLrNumber()) + 1;
            DecimalFormat fmt = new DecimalFormat("0000");
            return fmt.format(newLrNumber);
        } else {
            return "0001";
        }
    }
}
