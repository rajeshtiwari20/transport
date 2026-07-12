package com.jaijobner.transport_new.dto.bill;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BillResp {
    private Long id;
    private String billNumber;
    private LocalDate billDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer year;
    private String month;
    private String partyName;
    private String freight;
    private Double variationWeight;
    private Double shortage;
    private Double total;
}
