package com.jaijobner.transport_new.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "bill_details")
@Data
public class BillDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private BillEntity bill;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private TruckEntity truck;

    @Column(nullable = false)
    private String truckNum;

    @Column(nullable = false)
    private LocalDate lrDate;

    @Column(nullable = false)
    private String lrNum;

    private String fromName;

    private String toName;

    private String grade;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,3)")
    private Double weight = 0.0;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,3)")
    private Double unloadedWeight = 0.0;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,3)")
    private Double difference = 0.0;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,3)")
    private Double freight = 0.0;

    @Column(nullable = false)
    private Double amount = 0.0;

    @Column(nullable = false)
    private LocalDate unloadingDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
