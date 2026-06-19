package com.jaijobner.transport_new.dto.loading;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Date;

@Data
public class LoadingReq {
    @Min(1)
    private Integer pageNo = 1;
    @Min(1) @Max(1000)
    private Integer pageSize = 10;
    private String sortBy = "companyName";
    private String sortDirection = "ASC";
    private String searchTerm;
    private Date startDate;
    private Date endDate;
}
