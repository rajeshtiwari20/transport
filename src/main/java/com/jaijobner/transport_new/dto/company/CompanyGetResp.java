package com.jaijobner.transport_new.dto.company;

import com.jaijobner.transport_new.enums.CompanyType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyGetResp {
    private Long id;
    private String companyName;
    private String contactPersonName;
    private Long contactPersonMobile;
    private String companyCode;
    private String abbr;
    private CompanyType companyType;
    private String email;
    private Long mobile;
    private String address1;
    private String address2;
    private String district;
    private String state;
    private Integer pinNo;
    private String gstNo;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
