package com.jaijobner.transport_new.dto.party;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PartyReq {
    @Min(1)
    private Integer pageNo = 1;
    @Min(1) @Max(1000)
    private Integer pageSize = 500;
    private String sortBy = "partyName";
}
