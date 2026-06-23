package com.jaijobner.transport_new.dto.unloading;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Date;

@Data
public class UnloadingReq {
    @Min(1)
    private Integer pageNo = 1;
    @Min(1) @Max(1000)
    private Integer pageSize = 10;
    private String sortBy = "consigneeName";
    private String sortDirection = "ASC";
    private String searchTerm;
    private Date startDate;
    private Date endDate;
}
