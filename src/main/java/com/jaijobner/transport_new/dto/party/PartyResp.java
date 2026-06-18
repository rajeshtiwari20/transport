package com.jaijobner.transport_new.dto.party;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PartyResp {
    private Long id;
    private String partyName;
    private String partyCode;
    private String mobile;
    private LocalDateTime createdAt;
}
