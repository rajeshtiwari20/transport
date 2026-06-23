package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.unloading.UnloadingGetResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingWriteReq;
import com.jaijobner.transport_new.entity.UnloadingEntity;
import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.entity.PartyEntity;
import com.jaijobner.transport_new.entity.TruckEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnloadingMapper {
    @Mapping(target = "loading", source = "loadingEntity")
    @Mapping(target = "lrNumber", source = "loadingEntity.lrNumber")
    @Mapping(target = "unloadingDate", source = "req.unloadingDate")
    @Mapping(target = "truck", source = "truck")
    @Mapping(target = "truckNumber", source = "loadingEntity.truckNumber")
    @Mapping(target = "consignee", source = "consignee")
    @Mapping(target = "consigneeName", source = "loadingEntity.consigneeName")
    @Mapping(target = "unit", source = "req.unit")
    @Mapping(target = "weight", source = "req.weight")
    @Mapping(target = "unloadedWeight", source = "req.unloadedWeight")
    @Mapping(target = "changeInWeight", source = "req.changeInWeight")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UnloadingEntity toUnloadingEntityFromUnloadingWriteReq(UnloadingWriteReq req, LoadingEntity loadingEntity, TruckEntity truck, PartyEntity consignee, PartyEntity consignor);

    UnloadingResp toUnloadingRespFromUnladingEntity(UnloadingEntity entity);

    @Mapping(target = "truckId", source = "truck.id")
    @Mapping(target = "consigneeId", source = "consignee.id")
    UnloadingGetResp toUnloadingGetRespFromUnloadingEntity(UnloadingEntity entity);
}
