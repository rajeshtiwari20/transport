package com.jaijobner.transport_new.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loading_details")
@Data
public class LoadingDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loading_id")
    private LoadingEntity loading;

    @Column(nullable = false)
    private String fromName;

    @Column(nullable = false)
    private String toName;

    @Column(nullable = false)
    private String transportMode;

    @Lob
    private String consignorAddress;

    @Column(nullable = false)
    private String consignorGstNum;

    @Lob
    private String consigneeAddress;

    @Column(nullable = false)
    private String consigneeGstNum;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver1_id")
    private DriverEntity driver1;

    private String driver1Name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver2_id")
    private DriverEntity driver2;

    private String driver2Name;


    @Lob
    private String remarks;

    private Double cash;
    private Double qtyLtr;
    private Double rate;
    private Double amt;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
