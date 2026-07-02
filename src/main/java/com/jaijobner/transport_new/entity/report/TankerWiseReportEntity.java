package com.jaijobner.transport_new.entity.report;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Table(name = "v_tankerwiser_report")
@Getter
@Immutable
public class TankerWiseReportEntity {
    @Id
    private Long id;
    private String lrNumber;
    private String companyName;
    private String consigneeName;
    private String consignorName;
    private LocalDate lrDate;
    private String truckNumber;
    private String driver1name;
    private String fromName;
    private String toName;
    private Double weight;
    private Double unloadedWeight;
    private String unit;
    private LocalDate unloadingDate;
    private Double changeInWeight;
}
