package com.jaijobner.transport_new.dto.party;

import com.jaijobner.transport_new.enums.PartyType;
import lombok.Data;

@Data
public class PartyGetResp {
    private String partyName;
    private String partyCode;
    private PartyType partyType;
    private String email;
    private String mobile;
    private String telephone;
    private String address1;
    private String address2;
    private String district;
    private String state;
    private Integer pinNo;
    private String gstNo;
    private String panNo;
    private String accountNo;
    private String bankName;
    private String ifscCode;
    private String branchName;
}
