package com.jaijobner.transport_new.dto.driver;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverUpdateReq {
    @NotBlank(message = "Driver name is required")
    private String driverName;
    private String driverMobile;
    private String driverAddress;
}
