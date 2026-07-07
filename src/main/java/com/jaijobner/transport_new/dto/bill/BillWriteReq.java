package com.jaijobner.transport_new.dto.bill;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BillWriteReq {
    @NotEmpty(message = "Bill number is required")
    private String billNumber;

    @NotNull(message = "Bill date is required")
    private LocalDate billDate;

    private String freight;

    private Double shortage;

    @NotNull(message = "Total is required")
    private Double total;

    @NotNull(message = "Unloading is required")
    private Long unloadingId;

    @NotEmpty(message = "At least one bill detail is required")
    @Valid
    private List<BillDetailReq> billDetailReqList;

    @Data
    public static class BillDetailReq {
        private Long id;

        private Long unloadingId;

        private Long loadingId;

        @NotEmpty(message = "Truck number is required")
        private String truckNum;

        @NotNull(message = "LR date is required")
        private LocalDate lrDate;

        @NotEmpty(message = "LR number is required")
        private String lrNum;

        private String fromName;

        private String toName;

        private String grade;

        private Double weight = 0.0;

        private Double unloadedWeight = 0.0;

        private Double diff = 0.0;

        private Double freight = 0.0;

        private Double amount = 0.0;
    }
}
