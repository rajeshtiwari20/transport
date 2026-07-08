package com.jaijobner.transport_new.entity.report;

import jakarta.persistence.Column;
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

    private String truckNumber;

    private LocalDate unloadingDate;

    @Column(name = "driver_name")
    private String driverName;

    private String lrNumber;

    private String fromName;

    private String toName;

    private String consigneeName;

    private String consignorName;

    private String materialName;

    private Double weight;

    private Double unloadedWeight;

    private Double difference;
}
