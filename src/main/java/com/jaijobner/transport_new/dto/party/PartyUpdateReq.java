package com.jaijobner.transport_new.dto.party;

import com.jaijobner.transport_new.enums.PartyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PartyUpdateReq {
    @NotBlank(message = "Party name can not be null")
    private String partyName;
    private String partyCode;

    @NotNull(message = "Party type can not be null")
    private PartyType partyType;

    @NotBlank(message = "Email can not be empty")
    @Email(message = "Email is not valid")
    private String email;

    private String mobile;
    private String telephone;

    @NotBlank(message = "Address 1 can not be empty")
    private String address1;

    private String address2;
    private String district;

    @NotBlank(message = "State can not be empty")
    private String state;

    private Integer pinNo;

    @NotBlank(message = "GST number can not be empty")
    private String gstNo;

    private String panNo;

    private String accountNo;
    private String bankName;
    private String ifscCode;
    private String branchName;
}
