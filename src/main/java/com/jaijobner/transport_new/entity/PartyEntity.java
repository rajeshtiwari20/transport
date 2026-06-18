package com.jaijobner.transport_new.entity;

import com.jaijobner.transport_new.enums.PartyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "parties")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String partyName;

    private String partyCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyType partyType;

    private String email;
    private String mobile;
    private String telephone;

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

    private String panNo;
    private String accountNo;
    private String bankName;
    private String ifscCode;
    private String branchName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
