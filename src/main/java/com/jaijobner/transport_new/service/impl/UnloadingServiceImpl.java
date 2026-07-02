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
import com.jaijobner.transport_new.service.UnloadingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UnloadingServiceImpl implements UnloadingService {
    private final UnloadingRepository unloadingRepository;

    private final LoadingRepository loadingRepository;

    private final UnloadingMapper unloadingMapper;

    @Override
    public Page<UnloadingResp> getUnloadings(UnloadingReq req) {
        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo()-1, req.getPageSize(), sort);

        Specification<UnloadingEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

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

        return unloadingRepository.findAll(spec, pageable).map(unloadingMapper::toUnloadingRespFromUnladingEntity);
    }


    @Override
    @Transactional
    public void createUnloading(UnloadingWriteReq req) {
        Optional<LoadingEntity> loadingEntity = loadingRepository.findById(req.getLoadingId());

        if(loadingEntity.isPresent()) {
            LoadingEntity loading = loadingEntity.get();

            TruckEntity truck = loading.getTruck();
            PartyEntity consignee = loading.getConsignee();
            PartyEntity consignor = loading.getConsignor();

            UnloadingEntity unloadingEntity = unloadingMapper.toUnloadingEntityFromUnloadingWriteReq(req, loading, truck, consignee, consignor);

            unloadingRepository.save(unloadingEntity);
            loading.setStatus(LoadingStatus.UNLOADED);
            loading.setUnloadingId(unloadingEntity.getId());
            loadingRepository.save(loading);
        }

    }

    @Override
    public UnloadingGetResp getUnloading(Long id) {
        UnloadingEntity unloadingEntity =  unloadingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Unloading not found with id:" + id));
        return unloadingMapper.toUnloadingGetRespFromUnloadingEntity(unloadingEntity);
    }

    @Override
    @Transactional
    public void updateUnloading(Long id, UnloadingWriteReq req) {
        UnloadingEntity unloading = unloadingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Unloading not found with id:" + id));

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

        unloadingRepository.save(unloading);

    }
}
