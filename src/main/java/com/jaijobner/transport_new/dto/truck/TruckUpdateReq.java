package com.jaijobner.transport_new.dto.truck;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TruckUpdateReq {
    @NotBlank(message = "Truck number can not be null")
    private String truckNum;

    private String chasisNum;
    private String engineNum;
    private String capacity;
    private String model;

    private String ownerName;
    private Long ownerMobile;
}
