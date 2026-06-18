package com.jaijobner.transport_new.dto.truck;

import lombok.Data;

@Data
public class TruckResp {
    private Long id;
    private String truckNum;
    private String chasisNum;
    private String engineNum;
    private String capacity;
    private String model;
    private String ownerName;
    private Long ownerMobile;
}
