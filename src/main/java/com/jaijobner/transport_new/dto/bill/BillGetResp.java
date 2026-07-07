package com.jaijobner.transport_new.dto.bill;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BillGetResp {
    private Long id;
    private String billNumber;
    private LocalDate billDate;
    private String freight;
    private Double shortage;
    private Double total;
    private Long unloadingId;
    private List<BillDetailGetResp> billDetailList;

    @Data
    public static class BillDetailGetResp {
        private Long id;
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
    }
}
