package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.bill.BillGetResp;
import com.jaijobner.transport_new.dto.bill.BillReq;
import com.jaijobner.transport_new.dto.bill.BillResp;
import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.BillDetailEntity;
import com.jaijobner.transport_new.entity.BillEntity;
import com.jaijobner.transport_new.entity.UnloadingEntity;
import com.jaijobner.transport_new.mapper.BillDetailMapper;
import com.jaijobner.transport_new.mapper.BillMapper;
import com.jaijobner.transport_new.repository.BillDetailRepository;
import com.jaijobner.transport_new.repository.BillRepository;
import com.jaijobner.transport_new.repository.UnloadingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private UnloadingRepository unloadingRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillDetailMapper billDetailMapper;

    public Page<BillResp> getBills(BillReq req) {
        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo() - 1, req.getPageSize(), sort);

        Specification<BillEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getSearchTerm() != null && !req.getSearchTerm().trim().isEmpty()) {
                String pattern = "%" + req.getSearchTerm().trim().toLowerCase() + "%";
                Predicate billNumber = cb.like(cb.lower(root.get("billNumber")), pattern);
                Predicate freight = cb.like(cb.lower(root.get("freight")), pattern);
                predicates.add(cb.or(billNumber, freight));
            }

            if (req.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("billDate"), req.getStartDate()));
            }
            if (req.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("billDate"), req.getEndDate()));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };

        return billRepository.findAll(spec, pageable).map(billMapper::toBillResp);
    }

    @Transactional
    public void createBill(BillWriteReq req) {
        UnloadingEntity unloading = resolveUnloadingForBilling(req.getUnloadingId());

        BillEntity billEntity = billMapper.toBillEntity(req);
        billEntity.setUnloading(unloading);
        BillEntity savedBill = billRepository.save(billEntity);

        List<BillDetailEntity> details = req.getBillDetailReqList().stream()
                .map(billDetailMapper::toBillDetailEntity)
                .peek(detail -> detail.setBill(savedBill))
                .toList();
        billDetailRepository.saveAll(details);

        unloading.setBilledAt(LocalDateTime.now());
        unloadingRepository.save(unloading);
    }

    @Transactional(readOnly = true)
    public BillGetResp getBill(Long id) {
        BillEntity billEntity = billRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with id: " + id));
        return billMapper.toBillGetResp(billEntity);
    }

    @Transactional
    public void updateBill(Long id, BillWriteReq req) {
        BillEntity billEntity = billRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with id: " + id));

        updateBillUnloading(billEntity, req.getUnloadingId());
        billMapper.updateBillEntity(req, billEntity);
        billRepository.save(billEntity);

        Set<Long> incomingChildIds = req.getBillDetailReqList().stream()
                .map(BillWriteReq.BillDetailReq::getId)
                .filter(Objects::nonNull)
                .filter(childId -> childId > 0)
                .collect(Collectors.toSet());

        billEntity.getBillDetails().removeIf(detail -> !incomingChildIds.contains(detail.getId()));

        List<BillDetailEntity> details = req.getBillDetailReqList().stream()
                .map(detailReq -> {
                    if (detailReq.getId() != null && detailReq.getId() > 0) {
                        return billDetailMapper.toBillDetailEntityForUpdate(detailReq);
                    }
                    return billDetailMapper.toBillDetailEntity(detailReq);
                })
                .peek(detail -> detail.setBill(billEntity))
                .toList();

        billDetailRepository.saveAll(details);
    }

    private void updateBillUnloading(BillEntity billEntity, Long unloadingId) {
        UnloadingEntity currentUnloading = billEntity.getUnloading();
        if (currentUnloading != null && currentUnloading.getId().equals(unloadingId)) {
            return;
        }

        if (currentUnloading != null) {
            currentUnloading.setBilledAt(null);
            unloadingRepository.save(currentUnloading);
        }

        UnloadingEntity newUnloading = resolveUnloadingForBilling(unloadingId);
        newUnloading.setBilledAt(LocalDateTime.now());
        billEntity.setUnloading(newUnloading);
        unloadingRepository.save(newUnloading);
    }

    private UnloadingEntity resolveUnloadingForBilling(Long unloadingId) {
        UnloadingEntity unloading = unloadingRepository.findById(unloadingId)
                .orElseThrow(() -> new EntityNotFoundException("Unloading not found with id: " + unloadingId));

        if (unloading.getBilledAt() != null) {
            throw new IllegalStateException("Unloading is already billed with id: " + unloadingId);
        }

        return unloading;
    }
}
