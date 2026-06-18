package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.party.PartyGetResp;
import com.jaijobner.transport_new.dto.party.PartyResp;
import com.jaijobner.transport_new.entity.PartyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartyMapper {
    PartyResp partyEntityToPartyResp(PartyEntity partyEntity);

    PartyGetResp partyEntityToPartyGetResp(PartyEntity partyEntity);
}
