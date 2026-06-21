package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.loading.*;
import com.jaijobner.transport_new.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = {com.jaijobner.transport_new.enums.LoadingStatus.class})
public interface LoadingMapper {
    @Mapping(target = "company", source = "company")
    @Mapping(target = "companyName", source = "company.companyName")
    @Mapping(target = "truck", source = "truck")
    @Mapping(target = "truckNumber", source = "truck.truckNum")
    @Mapping(target = "consignee", source = "consignee")
    @Mapping(target = "consigneeName", source = "consignee.partyName")
    @Mapping(target = "consignor", source = "consignor")
    @Mapping(target = "consignorName", source = "consignor.partyName")
    @Mapping(target = "status", expression = "java(LoadingStatus.UNLOADING)")
    @Mapping(target = "lrNumber", ignore = true)
    @Mapping(target = "loadingDetails", ignore = true)
    @Mapping(target = "loadingMaterial", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoadingEntity toLoadingEntityFromLoadingCreateReq(LoadingCreateReq req, Company company, TruckEntity truck, PartyEntity consignee, PartyEntity consignor);

    LoadingResp toLoadingRespFromLoadingEntity(LoadingEntity entity);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "truckId", source = "truck.id")
    @Mapping(target = "consigneeId", source = "consignee.id")
    @Mapping(target = "consignorId", source = "consignor.id")
    @Mapping(target = "loadingDetails", source = "loadingDetails")
    @Mapping(target = "loadingMaterial", source = "loadingMaterial")
    LoadingGetResp toLoadingGetRespFromLoadingEntity(LoadingEntity loadingEntity);

    @Mapping(target = "driver1_id", source = "driver1.id")
    @Mapping(target = "driver2_id", source = "driver2.id")
    LoadingGetResp.LoadingDetailsGetResp toLoadingDetailsGetResp(LoadingDetailsEntity details);

    LoadingGetResp.LoadingMaterialGetResp toLoadingMaterialGetResp(LoadingMaterialEntity material);

    LoadingLRNumResp toLoadingLRNumResp(String lrNum);


    @Mapping(target = "company", source = "company")
    @Mapping(target = "companyName", source = "company.companyName")
    @Mapping(target = "truck", source = "truck")
    @Mapping(target = "truckNumber", source = "truck.truckNum")
    @Mapping(target = "consignee", source = "consignee")
    @Mapping(target = "consigneeName", source = "consignee.partyName")
    @Mapping(target = "consignor", source = "consignor")
    @Mapping(target = "consignorName", source = "consignor.partyName")
    @Mapping(target = "loadingDetails", ignore = true)
    @Mapping(target = "loadingMaterial", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void toLoadingEntityFromLoadingUpdateReq(LoadingUpdateReq req, Company company, TruckEntity truck, PartyEntity consignee, PartyEntity consignor, @MappingTarget LoadingEntity entity);


}
