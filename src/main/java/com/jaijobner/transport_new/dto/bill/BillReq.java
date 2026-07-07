package com.jaijobner.transport_new.dto.bill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BillReq {
    @Min(1)
    private Integer pageNo = 1;
    @Min(1) @Max(1000)
    private Integer pageSize = 10;
    private String sortBy = "billDate";
    private String sortDirection = "DESC";
    private String searchTerm;
    private LocalDate startDate;
    private LocalDate endDate;
}
