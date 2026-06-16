package com.jaijobner.transport_new.entity;

import com.jaijobner.transport_new.enums.CompanyType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    private String contactPersonName;
    private Long contactPersonMobile;
    private String companyCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType companyType;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Long mobile;

    @Column(nullable = false)
    private String address1;

    private String address2;
    private String district;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private Integer pinNo;

    @Column(nullable = false)
    private String gstNo;

    private String image;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
