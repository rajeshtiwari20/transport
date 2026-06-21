package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.loading.LoadingCreateReq;
import com.jaijobner.transport_new.dto.loading.LoadingUpdateReq;
import com.jaijobner.transport_new.entity.LoadingMaterialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoadingMaterialMapper {
    // Mapping used when creating materials: ignore id so new entities are created
    @Mapping(target = "loading", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoadingMaterialEntity toLoadingMaterialEntity(LoadingCreateReq.LoadingMaterialReq req);

    // Mapping used when updating materials: keep id so existing entities are updated
    @Mapping(target = "loading", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoadingMaterialEntity toLoadingMaterialEntity(LoadingUpdateReq.LoadingMaterialReq req);
}
