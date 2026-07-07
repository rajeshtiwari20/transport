package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.expense.ExpenseCreateReq;
import com.jaijobner.transport_new.dto.expense.ExpenseReq;
import com.jaijobner.transport_new.dto.expense.ExpenseUpdateReq;
import com.jaijobner.transport_new.entity.ExpenseEntity;
import com.jaijobner.transport_new.entity.TruckEntity;
import com.jaijobner.transport_new.mapper.ExpenseMapper;
import com.jaijobner.transport_new.repository.ExpenseRepository;
import com.jaijobner.transport_new.repository.TruckRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private ExpenseMapper expenseMapper;

    public Page<ExpenseEntity> getAllExpenses(ExpenseReq req) {
        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo() - 1, req.getPageSize(), sort);

        Specification<ExpenseEntity> spec = (root, query, cb) -> {
            if (req.getSearchTerm() == null || req.getSearchTerm().trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + req.getSearchTerm().trim().toLowerCase() + "%";

            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), pattern);
            Predicate typePredicate = cb.like(cb.lower(root.get("type")), pattern);
            Predicate remarksPredicate = cb.like(cb.lower(root.get("remarks")), pattern);
            Predicate truckNumberPredicate = cb.like(cb.lower(root.get("truckNumber")), pattern);

            return cb.or(titlePredicate, typePredicate, remarksPredicate, truckNumberPredicate);
        };

        return expenseRepository.findAll(spec, pageable);
    }

    public void createExpense(ExpenseCreateReq req) {
        TruckEntity truck = truckRepository.findById(req.getTruckId())
                .orElseThrow(() -> new EntityNotFoundException("Truck not found with id: " + req.getTruckId()));

        ExpenseEntity expenseEntity = expenseMapper.expenseCreateReqToExpenseEntity(req, truck);
        expenseRepository.save(expenseEntity);
    }

    public Optional<ExpenseEntity> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public void updateExpense(Long id, ExpenseUpdateReq req) {
        ExpenseEntity expenseEntity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        TruckEntity truck = truckRepository.findById(req.getTruckId())
                .orElseThrow(() -> new EntityNotFoundException("Truck not found with id: " + req.getTruckId()));

        expenseMapper.updateExpenseEntityFromUpdateReq(req, truck, expenseEntity);
        expenseRepository.save(expenseEntity);
    }
}
