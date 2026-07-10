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
import java.util.List;
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
                    BillEntity newBill = billMapper.toBillEntityFromLoadingAndUnloading(loading, unloading);
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
    public void createBill(BillWriteReq req) {
        log.info("Creating bill for unloading ID: {} with {} detail(s)",
                req.getUnloadingId(), req.getBillDetailReqList().size());

        try {
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

            log.info("Bill created with ID: {} for unloading ID: {}", savedBill.getId(), unloading.getId());
        } catch (Exception e) {
            log.error("Error occurred while creating bill for unloading ID: {}: {}",
                    req.getUnloadingId(), e.getMessage(), e);
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
        log.info("Updating bill ID: {} for unloading ID: {} with {} detail(s)",
                id, req.getUnloadingId(), req.getBillDetailReqList().size());

        BillEntity billEntity = billRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Bill not found with id: {}", id);
                    return new EntityNotFoundException("Bill not found with id: " + id);
                });

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

        recalculateBillTotals(billEntity);
        billRepository.save(billEntity);

        log.info("Bill updated successfully with ID: {}", id);
    }

    private void updateBillUnloading(BillEntity billEntity, Long unloadingId) {
        UnloadingEntity currentUnloading = billEntity.getUnloading();
        if (currentUnloading != null && currentUnloading.getId().equals(unloadingId)) {
            log.debug("Bill ID: {} already linked to unloading ID: {}, no change needed",
                    billEntity.getId(), unloadingId);
            return;
        }

        if (currentUnloading != null) {
            log.info("Removing billing link from unloading ID: {} for bill ID: {}",
                    currentUnloading.getId(), billEntity.getId());
            currentUnloading.setBilledAt(null);
            unloadingRepository.save(currentUnloading);
        }

        UnloadingEntity newUnloading = resolveUnloadingForBilling(unloadingId);
        newUnloading.setBilledAt(LocalDateTime.now());
        billEntity.setUnloading(newUnloading);
        unloadingRepository.save(newUnloading);

        log.info("Bill ID: {} linked to unloading ID: {}", billEntity.getId(), unloadingId);
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
