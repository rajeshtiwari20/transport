package com.jaijobner.transport_new.dto.report;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TankerWiseReportResp {
    private Long id;
    private String truckNumber;
    private LocalDate unloadingDate;
    private String driverName;
    private String lrNumber;
    private String fromName;
    private String toName;
    private String consigneeName;
    private String consignorName;
    private String materialName;
    private Double weight;
    private Double unloadedWeight;
    private Double difference;
    private Double freightRate;
    private Double freightAmount;
}
