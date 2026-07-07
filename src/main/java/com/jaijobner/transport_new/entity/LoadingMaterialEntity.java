package com.jaijobner.transport_new.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "loading_materials")
@Data
public class LoadingMaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loading_id", unique = true)
    private LoadingEntity loading;

    @Column(nullable = false)
    private String materialName;

    @Column(nullable = false)
    private String materialUnit;

    @Column(nullable = false)
    private Double loadedWeight;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private Double total;

    private String invoiceNum;

    private Date invoiceDate;

    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private Double invoiceValue = 0.0;

    private String eway;
    private Date ewayDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
