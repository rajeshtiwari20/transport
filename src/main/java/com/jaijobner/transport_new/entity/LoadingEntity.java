package com.jaijobner.transport_new.entity;

import com.jaijobner.transport_new.enums.LoadingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "loadings", uniqueConstraints =  @UniqueConstraint(columnNames = {"company_id", "lr_number"}))
@Data
public class LoadingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "loading", cascade = CascadeType.ALL)
    private LoadingDetailsEntity loadingDetails;

    @ToString.Exclude
    @OneToOne(mappedBy = "loading", cascade = CascadeType.ALL, orphanRemoval = true)
    private LoadingMaterialEntity loadingMaterial;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;


    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private Date lrDate;

    @Column(nullable = false)
    private String lrNumber;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double freightRate;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double freightAmount;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consignor_id")
    private PartyEntity consignor;

    @Column(nullable = false)
    private String consignorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadingStatus status;

    private Long unloadingId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
