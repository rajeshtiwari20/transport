package com.jaijobner.transport_new.dto.report;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TankerWiseReportResp {
    private Long id;
    private String lrNumber;
    private String companyName;
    private String consigneeName;
    private String consignorName;
    private LocalDate lrDate;
    private String truckNumber;
    private String driver1name;
    private String fromName;
    private String toName;
    private Double weight;
    private Double unloadedWeight;
    private String unit;
    private LocalDate unloadingDate;
    private Double changeInWeight;
}
