package com.jaijobner.transport_new.dto.loading;

import lombok.Data;

import java.util.Date;

@Data
public class LoadingUnloadingResp {
    private Long id;
    private Long companyId;
    private String companyName;
    private Long truckId;
    private Date lrDate;
    private String lrNumber;
    private Long consigneeId;
    private Long consignorId;
    private String consigneeName;
    private String consignorName;
    private String status;
    private String unit;
    private Double totalWeight;
}
