package com.jaijobner.transport_new.dto.loading;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jaijobner.transport_new.utils.FlexibleDoubleDeserializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class LoadingCreateReq {
    @NotNull(message = "Company is required")
    private Long companyId;

    @NotNull(message = "LR Date is required")
    private Date lrDate;

    @NotEmpty(message = "From is required")
    private String fromName;

    @NotEmpty(message = "To is required")
    private String toName;

    @NotEmpty(message = "Transport Mode is required")
    private String transportMode;

    @NotNull(message = "Consignee is required")
    private Long consigneeId;

    private String consigneeAddress;

    @NotEmpty(message = "Consignee GST Number is required")
    private String consigneeGstNum;

    @NotNull(message = "Consignor is required")
    private Long consignorId;

    private String consignorAddress;

    @NotEmpty(message = "Consignor GST Number is required")
    private String consignorGstNum;

    private String shipByParty;

    @NotNull(message = "Truck is required")
    private Long truckId;

    private Long driver1;
    private Long driver2;

    private String remarks;

    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double cash;

    @NotNull(message = "Freight Rate is required")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double freightRate;

    @NotNull(message = "Freight Amount is required")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double freightAmount;

    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double qtyLtr;

    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double rate;

    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double amt;

    @NotNull(message = "Loading material is required")
    private LoadingMaterialReq loadingMaterialReq;

    @Data
    public static class LoadingMaterialReq {
        @NotEmpty(message = "Material Name is required")
        private String materialName;

        @NotEmpty(message = "Material Unit is required")
        private String materialUnit;

        @NotNull(message = "Loaded Weight is required")
        @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
        private Double loadedWeight;

        @NotNull(message = "Loaded Rate is required")
        @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
        private Double rate;

        @NotNull(message = "Total Rate is required")
        @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
        private Double total;

        private String invoiceNum;

        private Date invoiceDate;

        @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
        private Double invoiceValue;

        private String eway;
        private Date ewayDate;
    }
}
