package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.company.CompanyCompactResp;
import com.jaijobner.transport_new.repository.projection.CompanyProjection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    List<CompanyCompactResp> toCompactRespList(List<CompanyProjection> projections);

    CompanyCompactResp toCompactResp(CompanyProjection projection);
}
