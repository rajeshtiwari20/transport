package com.jaijobner.transport_new.dto.expense;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseResp {
    private Long id;
    private String title;
    private Long truckId;
    private String truckNumber;
    private String type;
    private String remarks;
    private Double amount;
    private LocalDate expenseDate;
}
