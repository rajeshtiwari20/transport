package com.jaijobner.transport_new.service.impl;

import com.jaijobner.transport_new.dto.unloading.UnloadingGetResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingReq;
import com.jaijobner.transport_new.dto.unloading.UnloadingResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingWriteReq;
import com.jaijobner.transport_new.entity.UnloadingEntity;
import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.entity.TruckEntity;
import com.jaijobner.transport_new.entity.PartyEntity;
import com.jaijobner.transport_new.enums.LoadingStatus;
import com.jaijobner.transport_new.mapper.UnloadingMapper;
import com.jaijobner.transport_new.repository.UnloadingRepository;
import com.jaijobner.transport_new.repository.LoadingRepository;
import com.jaijobner.transport_new.service.BillService;
import com.jaijobner.transport_new.service.UnloadingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnloadingServiceImpl implements UnloadingService {
    private final UnloadingRepository unloadingRepository;

    private final LoadingRepository loadingRepository;

    private final UnloadingMapper unloadingMapper;

    private final BillService billService;

    @Override
    public Page<UnloadingResp> getUnloadings(UnloadingReq req) {
        return findUnloadings(req, false);
    }

    @Override
    public Page<UnloadingResp> getNonBilledUnloadings(UnloadingReq req) {
        return findUnloadings(req, true);
    }

    private Page<UnloadingResp> findUnloadings(UnloadingReq req, boolean nonBilledOnly) {
        log.info("Fetching {}unloadings - page: {}, size: {}, searchTerm: {}, startDate: {}, endDate: {}",
                nonBilledOnly ? "non-billed " : "", req.getPageNo(), req.getPageSize(),
                req.getSearchTerm(), req.getStartDate(), req.getEndDate());

        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo()-1, req.getPageSize(), sort);

        Specification<UnloadingEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nonBilledOnly) {
                predicates.add(cb.isNull(root.get("billedAt")));
            }

            if (req.getSearchTerm() != null && !req.getSearchTerm().trim().isEmpty()) {
                String pattern = "%" + req.getSearchTerm().trim().toLowerCase() + "%";
                Predicate lrNumber = cb.like(cb.lower(root.get("lrNumber")), pattern);
                Predicate truckNumber = cb.like(cb.lower(root.get("truckNumber")), pattern);
                Predicate consigneeName = cb.like(cb.lower(root.get("consigneeName")), pattern);
                predicates.add(cb.or(lrNumber, truckNumber, consigneeName));
            }

            if (req.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("unloadingDate"), req.getStartDate()));
            }
            if (req.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("unloadingDate"), req.getEndDate()));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<UnloadingResp> unloadings = unloadingRepository.findAll(spec, pageable)
                .map(unloadingMapper::toUnloadingRespFromUnladingEntity);
        log.info("Fetched {} unloadings out of {} total", unloadings.getNumberOfElements(), unloadings.getTotalElements());
        return unloadings;
    }


    @Override
    @Transactional
    public void createUnloading(UnloadingWriteReq req) {
        log.info("Creating unloading for loading ID: {}", req.getLoadingId());

        try {
            Optional<LoadingEntity> loadingEntity = loadingRepository.findById(req.getLoadingId());

            if (loadingEntity.isEmpty()) {
                log.warn("Loading not found with id: {}", req.getLoadingId());
                return;
            }

            LoadingEntity loading = loadingEntity.get();

            TruckEntity truck = loading.getTruck();
            PartyEntity consignee = loading.getConsignee();
            PartyEntity consignor = loading.getConsignor();

            UnloadingEntity unloadingEntity = unloadingMapper.toUnloadingEntityFromUnloadingWriteReq(req, loading, truck, consignee, consignor);

            UnloadingEntity savedUnloading = unloadingRepository.save(unloadingEntity);
            log.info("Unloading created with ID: {} for LR: {}", savedUnloading.getId(), loading.getLrNumber());

            loading.setStatus(LoadingStatus.UNLOADED);
            loading.setUnloadingId(unloadingEntity.getId());
            LoadingEntity savedLoading = loadingRepository.save(loading);
            log.info("Loading ID: {} marked as UNLOADED", savedLoading.getId());

            billService.createBill(savedLoading, savedUnloading);
            log.info("Unloading creation completed for loading ID: {}", req.getLoadingId());
        } catch (Exception e) {
            log.error("Error occurred while creating unloading for loading ID: {}: {}",
                    req.getLoadingId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public UnloadingGetResp getUnloading(Long id) {
        log.info("Fetching unloading with ID: {}", id);

        UnloadingEntity unloadingEntity = unloadingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Unloading not found with id: {}", id);
                    return new EntityNotFoundException("Unloading not found with id:" + id);
                });

        log.info("Unloading found with ID: {}, LR number: {}", id, unloadingEntity.getLrNumber());
        return unloadingMapper.toUnloadingGetRespFromUnloadingEntity(unloadingEntity);
    }

    @Override
    @Transactional
    public void updateUnloading(Long id, UnloadingWriteReq req) {
        log.info("Updating unloading with ID: {}", id);

        UnloadingEntity unloading = unloadingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Unloading not found with id: {}", id);
                    return new EntityNotFoundException("Unloading not found with id:" + id);
                });

        unloading.setUnloadingDate(req.getUnloadingDate());
        unloading.setRemarks(req.getRemarks());
        unloading.setCash(req.getCash());
        unloading.setQty(req.getQty());
        unloading.setRateLtr(req.getRateLtr());
        unloading.setAmt(req.getAmt());
        unloading.setUnloadedWeight(req.getUnloadedWeight());
        unloading.setChangeInWeight(req.getChangeInWeight());
        unloading.setRate(req.getRate());
        unloading.setGrFreight(req.getGrFreight());

        UnloadingEntity savedUnloading = unloadingRepository.save(unloading);

        LoadingEntity loading = loadingRepository.findById(req.getLoadingId())
                .orElseThrow(() -> {
                    log.warn("Loading not found with id: {}", req.getLoadingId());
                    return new EntityNotFoundException("Loading not found with id:" + req.getLoadingId());
                });

        if (savedUnloading.getBilledAt() != null) {
            billService.updateBill(loading, savedUnloading);
            log.info("Bill updated for unloading ID: {}", id);
        } else {
            log.info("Unloading ID: {} is not billed yet, skipping bill update", id);
        }

        log.info("Unloading updated successfully with ID: {}", id);
    }
}
