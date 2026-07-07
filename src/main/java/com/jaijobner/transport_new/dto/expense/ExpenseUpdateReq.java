package com.jaijobner.transport_new.dto.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseUpdateReq {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Truck is required")
    private Long truckId;

    private String type;

    private String remarks;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;
}
