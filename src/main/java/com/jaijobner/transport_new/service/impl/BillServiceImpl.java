package com.jaijobner.transport_new.service.impl;

import com.jaijobner.transport_new.dto.bill.BillGetResp;
import com.jaijobner.transport_new.dto.bill.BillReq;
import com.jaijobner.transport_new.dto.bill.BillResp;
import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.BillDetailEntity;
import com.jaijobner.transport_new.entity.BillEntity;
import com.jaijobner.transport_new.entity.Company;
import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.entity.PartyEntity;
import com.jaijobner.transport_new.entity.UnloadingEntity;
import com.jaijobner.transport_new.mapper.BillDetailMapper;
import com.jaijobner.transport_new.mapper.BillMapper;
import com.jaijobner.transport_new.repository.BillDetailRepository;
import com.jaijobner.transport_new.repository.BillRepository;
import com.jaijobner.transport_new.repository.UnloadingRepository;
import com.jaijobner.transport_new.service.BillService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final UnloadingRepository unloadingRepository;
    private final BillMapper billMapper;
    private final BillDetailMapper billDetailMapper;

    @Override
    public Page<BillResp> getBills(BillReq req) {
        log.info("Fetching bills - page: {}, size: {}, searchTerm: {}, startDate: {}, endDate: {}",
                req.getPageNo(), req.getPageSize(), req.getSearchTerm(), req.getStartDate(), req.getEndDate());

        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo() - 1, req.getPageSize(), sort);

        Specification<BillEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getSearchTerm() != null && !req.getSearchTerm().trim().isEmpty()) {
                String pattern = "%" + req.getSearchTerm().trim().toLowerCase() + "%";
                Join<BillEntity, PartyEntity> consignee = root.join("consignee", JoinType.LEFT);
                Predicate billNumber = cb.like(cb.lower(root.get("billNumber")), pattern);
                Predicate partyName = cb.like(cb.lower(consignee.get("partyName")), pattern);
                predicates.add(cb.or(billNumber, partyName));
            }

            if (req.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("billDate"), req.getStartDate()));
            }
            if (req.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("billDate"), req.getEndDate()));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<BillResp> bills = billRepository.findAll(spec, pageable).map(billMapper::toBillResp);
        log.info("Fetched {} bills out of {} total", bills.getNumberOfElements(), bills.getTotalElements());
        return bills;
    }

    @Override
    @Transactional
    public void createBill(LoadingEntity loading, UnloadingEntity unloading) {
        log.info("Creating bill from loading ID: {} and unloading ID: {}", loading.getId(), unloading.getId());

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        Year currentYear = Year.now();
        int currentMonth = LocalDate.now().getMonthValue();
        PartyEntity consignee = loading.getConsignee();

        BillEntity billEntity = billRepository.findByConsigneeAndYearAndMonth(consignee, currentYear, currentMonth)
                .map(existingBill -> {
                    log.info("Using existing bill ID: {} for consignee ID: {}, year: {}, month: {}",
                            existingBill.getId(), consignee.getId(), currentYear, currentMonth);
                    return existingBill;
                })
                .orElseGet(() -> {
                    log.info("No existing bill found for consignee ID: {}, year: {}, month: {}. Creating new bill.",
                            consignee.getId(), currentYear, currentMonth);
                    BillEntity newBill = billMapper.toBillEntityFromLoading(loading);
                    newBill.setStartDate(firstDayOfMonth);
                    newBill.setEndDate(lastDayOfMonth);
                    newBill.setBillDate(lastDayOfMonth);
                    newBill.setConsignee(consignee);
                    newBill.setYear(currentYear);
                    newBill.setMonth(currentMonth);
                    newBill.setBillNumber(generateBillNumber(loading.getCompany()));
                    newBill.setTotalAmount(0.0);
                    BillEntity savedBill = billRepository.save(newBill);
                    log.info("New bill created with ID: {}, bill number: {}", savedBill.getId(), savedBill.getBillNumber());
                    return savedBill;
                });

        BillDetailEntity detail = billDetailMapper.toBillDetailEntityFromLoadingAndUnloading(loading, unloading);
        detail.setBill(billEntity);
        billEntity.getBillDetails().add(detail);
        billDetailRepository.save(detail);

        unloading.setBilledAt(LocalDateTime.now());
        unloadingRepository.save(unloading);

        recalculateBillTotals(billEntity);
        billRepository.save(billEntity);

        log.info("Bill detail added to bill ID: {} for unloading ID: {}. Total amount: {}, freight: {}",
                billEntity.getId(), unloading.getId(), billEntity.getTotalAmount(), billEntity.getFreight());
    }

    @Override
    @Transactional
    public void updateBill(LoadingEntity loading, UnloadingEntity unloading) {
        log.info("Updating bill from loading ID: {} and unloading ID: {}", loading.getId(), unloading.getId());

        BillDetailEntity detail = billDetailRepository.findByUnloadingId(unloading.getId())
                .orElseThrow(() -> {
                    log.warn("Bill detail not found for unloading ID: {}", unloading.getId());
                    return new EntityNotFoundException("Bill detail not found for unloading id: " + unloading.getId());
                });

        billDetailMapper.updateBillDetailEntityFromLoadingAndUnloading(loading, unloading, detail);
        billDetailRepository.save(detail);

        BillEntity billEntity = detail.getBill();
        recalculateBillTotals(billEntity);
        billRepository.save(billEntity);

        log.info("Bill detail updated for bill ID: {} and unloading ID: {}. Total amount: {}, freight: {}",
                billEntity.getId(), unloading.getId(), billEntity.getTotalAmount(), billEntity.getFreight());
    }

    @Override
    @Transactional
    public void createBill(BillWriteReq req) {
        log.info("Creating bill with {} detail(s)", req.getBillDetailReqList().size());

        try {
            BillEntity billEntity = billMapper.toBillEntity(req);
            BillEntity savedBill = billRepository.save(billEntity);

            List<BillDetailEntity> details = req.getBillDetailReqList().stream()
                    .map(detailReq -> {
                        BillDetailEntity detail = billDetailMapper.toBillDetailEntity(detailReq);
                        linkDetailUnloading(detail, detailReq.getUnloadingId(), null);
                        detail.setBill(savedBill);
                        return detail;
                    })
                    .toList();
            billDetailRepository.saveAll(details);

            log.info("Bill created with ID: {}", savedBill.getId());
        } catch (Exception e) {
            log.error("Error occurred while creating bill: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BillGetResp getBill(Long id) {
        log.info("Fetching bill with ID: {}", id);

        BillEntity billEntity = billRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Bill not found with id: {}", id);
                    return new EntityNotFoundException("Bill not found with id: " + id);
                });

        log.info("Bill found with ID: {}, bill number: {}", id, billEntity.getBillNumber());
        return billMapper.toBillGetResp(billEntity);
    }

    @Override
    @Transactional
    public void updateBill(Long id, BillWriteReq req) {
        log.info("Updating bill ID: {} with {} detail(s)", id, req.getBillDetailReqList().size());

        BillEntity billEntity = billRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Bill not found with id: {}", id);
                    return new EntityNotFoundException("Bill not found with id: " + id);
                });

        billMapper.updateBillEntity(req, billEntity);
        billRepository.save(billEntity);

        Set<Long> incomingChildIds = req.getBillDetailReqList().stream()
                .map(BillWriteReq.BillDetailReq::getId)
                .filter(Objects::nonNull)
                .filter(childId -> childId > 0)
                .collect(Collectors.toSet());

        Map<Long, UnloadingEntity> existingUnloadingsByDetailId = new HashMap<>();
        for (BillDetailEntity detail : billEntity.getBillDetails()) {
            if (detail.getId() != null) {
                existingUnloadingsByDetailId.put(detail.getId(), detail.getUnloading());
            }
            if (!incomingChildIds.contains(detail.getId())) {
                clearDetailBilling(detail);
            }
        }

        billEntity.getBillDetails().removeIf(detail -> !incomingChildIds.contains(detail.getId()));

        List<BillDetailEntity> details = req.getBillDetailReqList().stream()
                .map(detailReq -> {
                    BillDetailEntity detail;
                    if (detailReq.getId() != null && detailReq.getId() > 0) {
                        detail = billDetailMapper.toBillDetailEntityForUpdate(detailReq);
                        linkDetailUnloading(detail, detailReq.getUnloadingId(),
                                existingUnloadingsByDetailId.get(detailReq.getId()));
                    } else {
                        detail = billDetailMapper.toBillDetailEntity(detailReq);
                        linkDetailUnloading(detail, detailReq.getUnloadingId(), null);
                    }
                    detail.setBill(billEntity);
                    return detail;
                })
                .toList();

        billDetailRepository.saveAll(details);

        recalculateBillTotals(billEntity);
        billRepository.save(billEntity);

        log.info("Bill updated successfully with ID: {}", id);
    }

    private void linkDetailUnloading(BillDetailEntity detail, Long unloadingId, UnloadingEntity previousUnloading) {
        if (unloadingId == null) {
            throw new IllegalArgumentException("Unloading is required for bill detail");
        }

        if (previousUnloading != null && previousUnloading.getId().equals(unloadingId)) {
            detail.setUnloading(previousUnloading);
            return;
        }

        if (previousUnloading != null) {
            log.info("Removing billing link from unloading ID: {}", previousUnloading.getId());
            previousUnloading.setBilledAt(null);
            unloadingRepository.save(previousUnloading);
        }

        UnloadingEntity unloading = resolveUnloadingForBilling(unloadingId);
        unloading.setBilledAt(LocalDateTime.now());
        unloadingRepository.save(unloading);
        detail.setUnloading(unloading);

        log.info("Bill detail linked to unloading ID: {}", unloadingId);
    }

    private void clearDetailBilling(BillDetailEntity detail) {
        UnloadingEntity unloading = detail.getUnloading();
        if (unloading != null) {
            log.info("Clearing billing link from unloading ID: {} for removed bill detail ID: {}",
                    unloading.getId(), detail.getId());
            unloading.setBilledAt(null);
            unloadingRepository.save(unloading);
        }
    }

    private UnloadingEntity resolveUnloadingForBilling(Long unloadingId) {
        log.debug("Resolving unloading ID: {} for billing", unloadingId);

        UnloadingEntity unloading = unloadingRepository.findById(unloadingId)
                .orElseThrow(() -> {
                    log.warn("Unloading not found with id: {}", unloadingId);
                    return new EntityNotFoundException("Unloading not found with id: " + unloadingId);
                });

        if (unloading.getBilledAt() != null) {
            log.warn("Unloading ID: {} is already billed at {}", unloadingId, unloading.getBilledAt());
            throw new IllegalStateException("Unloading is already billed with id: " + unloadingId);
        }

        return unloading;
    }

    private void recalculateBillTotals(BillEntity billEntity) {
        double totalDifference = billEntity.getBillDetails().stream()
                .map(BillDetailEntity::getDifference)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum);

        double totalFreight = billEntity.getBillDetails().stream()
                .map(BillDetailEntity::getAmount)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum);

        billEntity.setIsShortage(billEntity.getBillDetails().stream()
                .anyMatch(billDetail -> billDetail.getDifference() != null && billDetail.getDifference() != 0));

        double variationRate = billEntity.getVariationRate() != null ? billEntity.getVariationRate() : 0.0;
        double variationAmount = totalDifference * variationRate;

        boolean isShortage = billEntity.getIsShortage() != null && billEntity.getIsShortage();
        double totalAmount = isShortage ? totalFreight - variationAmount : totalFreight + variationAmount;

        billEntity.setFreight(totalFreight);
        billEntity.setVariationWeight(totalDifference);
        billEntity.setTotalAmount(totalAmount);
    }

    private String generateBillNumber(Company company) {
        if (company == null) {
            throw new IllegalStateException("Company is required to generate bill number");
        }
        if (company.getAbbr() == null || company.getAbbr().isBlank()) {
            throw new IllegalStateException("Company abbreviation is not set for company id: " + company.getId());
        }

        String prefix = company.getAbbr() + "/" + getFinancialYearRange() + "/";
        long nextSequence = billRepository.findFirstByBillNumberStartingWithOrderByIdDesc(prefix)
                .map(BillEntity::getBillNumber)
                .map(billNumber -> {
                    try {
                        return Long.parseLong(billNumber.substring(prefix.length())) + 1;
                    } catch (NumberFormatException e) {
                        log.warn("Could not parse bill sequence from bill number: {}", billNumber);
                        return 1L;
                    }
                })
                .orElse(1L);

        String billNumber = prefix + new DecimalFormat("000").format(nextSequence);
        log.debug("Generated bill number: {} for company ID: {}", billNumber, company.getId());
        return billNumber;
    }

    private String getFinancialYearRange() {
        LocalDate today = LocalDate.now();
        int startYear;
        int endYear;

        if (today.getMonthValue() >= Month.APRIL.getValue()) {
            startYear = today.getYear();
            endYear = today.getYear() + 1;
        } else {
            startYear = today.getYear() - 1;
            endYear = today.getYear();
        }

        return String.format("%02d-%02d", startYear % 100, endYear % 100);
    }
}
