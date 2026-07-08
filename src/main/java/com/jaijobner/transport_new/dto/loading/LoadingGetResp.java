package com.jaijobner.transport_new.dto.loading;

import lombok.Data;

import java.util.Date;

@Data
public class LoadingGetResp {
    private Long id;
    private Long companyId;
    private String companyName;
    private Long truckId;
    private Date lrDate;
    private String lrNumber;
    private Double freightRate;
    private Double freightAmount;
    private Long consigneeId;
    private Long consignorId;
    private String consigneeName;
    private String consignorName;
    private String status;
    private LoadingDetailsGetResp loadingDetails;
    private LoadingMaterialGetResp loadingMaterial;

    @Data
    public static class LoadingDetailsGetResp {
        private Long id;
        private String fromName;
        private String toName;
        private String transportMode;
        private String consignorAddress;
        private String consignorGstNum;
        private String consigneeAddress;
        private String consigneeGstNum;
        private String shipByParty;
        private Long driver1_id;
        private String driver1Name;
        private Long driver2_id;
        private String driver2Name;
        private String remarks;
        private Double cash;
        private Double qtyLtr;
        private Double rate;
        private Double amt;
    }

    @Data
    public static class LoadingMaterialGetResp {
        private Long id;
        private String materialName;
        private String materialUnit;
        private Double loadedWeight;
        private Double rate;
        private Double total;
        private String invoiceNum;
        private Date invoiceDate;
        private Double invoiceValue;
        private String eway;
        private Date ewayDate;
    }
}
