package com.jaijobner.transport_new.dto.unloading;

import lombok.Data;

import java.util.Date;

@Data
public class UnloadingGetResp {
    private Long id;
    private Long loadingId;
    private String lrNumber;
    private Date unloadingDate;
    private Long truckId;
    private String truckNumber;
    private Long consigneeId;
    private String consigneeName;
    private String unit;
    private Double weight;
    private Double unloadedWeight;
    private Double changeInWeight;
    private Double rate;
    private Integer grFreight;
    private String remarks;
    private Double cash;
    private Double qty;
    private Double rateLtr;
    private Double amt;
}
