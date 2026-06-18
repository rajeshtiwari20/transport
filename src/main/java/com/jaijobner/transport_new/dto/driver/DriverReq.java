package com.jaijobner.transport_new.dto.driver;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DriverReq {
    @Min(1)
    private Integer pageNo = 1;
    @Min(1) @Max(1000)
    private Integer pageSize = 10;
    private String sortBy = "driverName";
    private String sortDirection = "ASC";
    private String searchTerm;
}
