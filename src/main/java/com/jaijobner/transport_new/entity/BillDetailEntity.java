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

    @Column(nullable = false)
    private String truckNum;

    @Column(nullable = false)
    private LocalDate lrDate;

    @Column(nullable = false)
    private String lrNum;

    private String fromName;

    private String toName;

    private String grade;

    @Column(nullable = false)
    private Double weight = 0.0;

    @Column(nullable = false)
    private Double unloadedWeight = 0.0;

    @Column(nullable = false)
    private Double diff = 0.0;

    @Column(nullable = false)
    private Double freight = 0.0;

    @Column(nullable = false)
    private Double amount = 0.0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
