package com.jaijobner.transport_new.dto.unloading;

import lombok.Data;

@Data
public class UnloadingResp {
    private Long id;
    private String lrNumber;
    private String truckNumber;
    private String consigneeName;
    private String unit;
    private Double weight;
    private Double unloadedWeight;
    private Double changeInWeight;
}
