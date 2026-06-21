package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.party.PartyCompactResp;
import com.jaijobner.transport_new.dto.party.PartyGetResp;
import com.jaijobner.transport_new.dto.party.PartyResp;
import com.jaijobner.transport_new.entity.PartyEntity;
import com.jaijobner.transport_new.repository.projection.PartyProjection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartyMapper {
    PartyResp partyEntityToPartyResp(PartyEntity partyEntity);

    PartyGetResp partyEntityToPartyGetResp(PartyEntity partyEntity);

    List<PartyCompactResp> toCompactResp(List<PartyProjection> projectionList);

    PartyCompactResp toPartyCompactResp(PartyProjection partyProjection);
}
