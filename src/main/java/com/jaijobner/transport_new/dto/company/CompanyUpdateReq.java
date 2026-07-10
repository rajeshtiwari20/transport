package com.jaijobner.transport_new.dto.company;

import com.jaijobner.transport_new.enums.CompanyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyUpdateReq {
    @NotBlank(message = "Company name can not be null")
    private String companyName;
    private String companyCode;
    private String abbr;

    private String contactPersonName;
    private Long contactPersonMobile;

    @NotNull(message = "Company type can not be null")
    private CompanyType companyType;

    @NotBlank(message = "Email can not be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "Mobile number can not be null")
    private Long mobile;

    @NotBlank(message = "Address 1 can not be empty")
    private String address1;

    private String address2;
    private String district;

    @NotBlank(message = "State can not be empty")
    private String state;

    @NotBlank(message = "GST number can not be empty")
    private String gstNo;

    @NotNull(message = "PIN number can not be empty")
    private Integer pinNo;
}
