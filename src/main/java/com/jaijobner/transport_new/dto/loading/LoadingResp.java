package com.jaijobner.transport_new.dto.loading;

import lombok.Data;

import java.util.Date;

@Data
public class LoadingResp {
    private Long id;
    private String companyName;
    private Date lrDate;
    private String lrNumber;
    private String truckNumber;
    private String consigneeName;
    private String consignorName;
    private Long unloadingId;
    private String status;
}
