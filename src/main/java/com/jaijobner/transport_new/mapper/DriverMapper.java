package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.driver.DriverResp;
import com.jaijobner.transport_new.dto.driver.DriverCreateReq;
import com.jaijobner.transport_new.dto.driver.DriverUpdateReq;
import com.jaijobner.transport_new.entity.DriverEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverResp driverEntityToDriverResp(DriverEntity driver);

    DriverEntity driverCreateReqToDriverEntity(DriverCreateReq req);

    void updateDriverEntityFromUpdateReq(@MappingTarget DriverEntity driverEntity, DriverUpdateReq req);
}
