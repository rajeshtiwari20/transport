package com.jaijobner.transport_new.dto.report;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportReq {
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;
    @NotNull(message = "End Date is required")
    private LocalDate endDate;
    private String truckNumber;
    private String partyName;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
