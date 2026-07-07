package com.jaijobner.transport_new.dto.bill;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BillResp {
    private Long id;
    private String billNumber;
    private LocalDate billDate;
    private String freight;
    private Double shortage;
    private Double total;
    private Long unloadingId;
}
