package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.company.CompanyCompactResp;
import com.jaijobner.transport_new.dto.company.CompanyGetResp;
import com.jaijobner.transport_new.entity.Company;
import com.jaijobner.transport_new.repository.projection.CompanyProjection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyGetResp companyEntityToCompanyGetResp(Company company);

    List<CompanyCompactResp> toCompactRespList(List<CompanyProjection> projections);

    CompanyCompactResp toCompactResp(CompanyProjection projection);
}
