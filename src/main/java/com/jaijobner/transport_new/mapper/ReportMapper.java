package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.report.TankerWiseReportResp;
import com.jaijobner.transport_new.entity.report.TankerWiseReportEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    List<TankerWiseReportResp> toTankerWiseReportRespFromTankerWiseReportEntity(List<TankerWiseReportEntity> entity);

    TankerWiseReportResp toTankerWiseReportResp(TankerWiseReportEntity entity);
}
