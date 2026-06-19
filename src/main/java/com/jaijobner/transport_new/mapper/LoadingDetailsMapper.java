package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.loading.LoadingCreateReq;
import com.jaijobner.transport_new.entity.DriverEntity;
import com.jaijobner.transport_new.entity.LoadingDetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoadingDetailsMapper {
    @Mapping(target = "loading", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "driver1", source = "driver1")
    @Mapping(target = "driver1Name", source = "driver1.driverName")
    @Mapping(target = "driver2", source = "driver2")
    @Mapping(target = "driver2Name", source = "driver2.driverName")
    @Mapping(target = "fromName", source = "req.fromName")
    @Mapping(target = "toName", source = "req.toName")
    @Mapping(target = "transportMode", source = "req.transportMode")
    @Mapping(target = "consignorAddress", source = "req.consignorAddress")
    @Mapping(target = "consignorGstNum", source = "req.consignorGstNum")
    @Mapping(target = "consigneeAddress", source = "req.consigneeAddress")
    @Mapping(target = "consigneeGstNum", source = "req.consigneeGstNum")
    @Mapping(target = "remarks", source = "req.remarks")
    @Mapping(target = "cash", source = "req.cash")
    @Mapping(target = "qtyLtr", source = "req.qtyLtr")
    @Mapping(target = "rate", source = "req.rate")
    @Mapping(target = "amt", source = "req.amt")
    LoadingDetailsEntity toLoadingDetailsEntity(LoadingCreateReq req, DriverEntity driver1, DriverEntity driver2);
}
