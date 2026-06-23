package com.jaijobner.transport_new.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "unloadings")

@Data
public class UnloadingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loading_id")
    private LoadingEntity loading;

    @Column(nullable = false)
    private String lrNumber;

    @Column(nullable = false)
    private Date unloadingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private TruckEntity truck;

    @Column(nullable = false)
    private String truckNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consignee_id")
    private PartyEntity consignee;

    @Column(nullable = false)
    private String consigneeName;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double unloadedWeight;

    private Double changeInWeight;

    private Double rate;

    private Integer grFreight;

    @Lob
    private String remarks;

    private Double cash;
    private Double qty;
    private Double rateLtr;
    private Double amt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
