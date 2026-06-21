package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.truck.TruckCompactResp;
import com.jaijobner.transport_new.dto.truck.TruckCreateReq;
import com.jaijobner.transport_new.dto.truck.TruckResp;
import com.jaijobner.transport_new.dto.truck.TruckUpdateReq;
import com.jaijobner.transport_new.entity.TruckEntity;
import com.jaijobner.transport_new.repository.projection.TruckProjection;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TruckMapper {
    TruckResp truckEntityToTruckResp(TruckEntity truck);

    TruckEntity truckCreateReqToTruckEntity(TruckCreateReq req);

    void updateTruckEntityFromUpdateReq(@MappingTarget TruckEntity truckEntity, TruckUpdateReq req);

    List<TruckCompactResp> toCompactResp(List<TruckProjection> projectionList);

    TruckCompactResp toTruckCompactResp(TruckProjection projection);
}
