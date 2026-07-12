package com.jaijobner.transport_new.dto.bill;

import com.jaijobner.transport_new.dto.party.PartyGetResp;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillGetResp {
    private Long id;
    private Integer year;
    private Integer month;
    private LocalDate startDate;
    private LocalDate endDate;
    private String billNumber;
    private LocalDate billDate;
    private String freight;
    private Double variationRate;
    private Double variationWeight;
    private Double shortage;
    private Double total;
    private PartyGetResp consignee;
    private List<BillDetailGetResp> billDetailList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class BillDetailGetResp {
        private Long id;
        private Long truckId;
        private String truckNum;
        private LocalDate lrDate;
        private String lrNum;
        private String fromName;
        private String toName;
        private String grade;
        private Double weight;
        private Double unloadedWeight;
        private Double diff;
        private Double freight;
        private Double amount;
        private Long unloadingId;
        private LocalDate unloadingDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
