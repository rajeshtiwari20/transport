package com.jaijobner.transport_new.dto.unloading;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class UnloadingWriteReq {
    @NotNull(message = "Loading is required")
    private Long loadingId;

    @NotNull(message = "Unloading date is required")
    private Date unloadingDate;

    @NotEmpty(message = "Unit is required")
    private String unit;

    @NotNull(message = "Weight date is required")
    private Double weight;

    @NotNull(message = "Uploaded weight date is required")
    private Double unloadedWeight;

    private Double changeInWeight = 0.0;

    private Double rate = 0.0;

    private Integer grFreight = 0;

    private String remarks;

    private Double cash;
    private Double qty;
    private Double rateLtr;
    private Double amt;
}
