package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.loading.LoadingCreateReq;
import com.jaijobner.transport_new.dto.loading.LoadingUpdateReq;
import com.jaijobner.transport_new.entity.LoadingMaterialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LoadingMaterialMapper {
    @Mapping(target = "loading", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "invoiceValue", defaultValue = "0.0")
    LoadingMaterialEntity toLoadingMaterialEntity(LoadingCreateReq.LoadingMaterialReq req);

    @Mapping(target = "loading", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "invoiceValue", defaultValue = "0.0")
    @Mapping(target = "rate", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "total", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toLoadingMaterialEntityFromLoadingUpdateReq(LoadingUpdateReq.LoadingMaterialReq req, @MappingTarget LoadingMaterialEntity entity);
}
