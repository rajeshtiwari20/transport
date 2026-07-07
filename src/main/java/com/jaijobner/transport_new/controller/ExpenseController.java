package com.jaijobner.transport_new.controller;

import com.jaijobner.transport_new.dto.ApiResponse;
import com.jaijobner.transport_new.dto.expense.ExpenseCreateReq;
import com.jaijobner.transport_new.dto.expense.ExpenseReq;
import com.jaijobner.transport_new.dto.expense.ExpenseResp;
import com.jaijobner.transport_new.dto.expense.ExpenseUpdateReq;
import com.jaijobner.transport_new.mapper.ExpenseMapper;
import com.jaijobner.transport_new.service.ExpenseService;
import com.jaijobner.transport_new.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseMapper expenseMapper;

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<Page<ExpenseResp>>> getExpenses(@Valid @RequestBody ExpenseReq req) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            var expenses = expenseService.getAllExpenses(req).map(expenseMapper::expenseEntityToExpenseResp);
            return ResponseEntity.ok(ApiResponse.success("Expenses retrieved successfully", expenses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving expenses"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createExpense(@Valid @RequestBody ExpenseCreateReq req) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            expenseService.createExpense(req);
            return ResponseEntity.ok(ApiResponse.success("Expense created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while creating the expense"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResp>> getExpense(@PathVariable Long id) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            ExpenseResp expense = expenseService.getExpenseById(id)
                    .map(expenseMapper::expenseEntityToExpenseResp)
                    .orElse(null);
            if (expense == null) {
                return ResponseEntity.status(404).body(ApiResponse.fail("Expense not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Expense retrieved successfully", expense));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while retrieving the expense"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseUpdateReq req) {
        if (!SecurityUtils.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Unauthorized"));
        }

        try {
            expenseService.updateExpense(id, req);
            return ResponseEntity.ok(ApiResponse.success("Expense updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("An error occurred while updating the expense"));
        }
    }
}
