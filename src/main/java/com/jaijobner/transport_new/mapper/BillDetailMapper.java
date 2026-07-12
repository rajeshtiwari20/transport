package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.BillDetailEntity;
import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.entity.UnloadingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface BillDetailMapper {
    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "truck", ignore = true)
    @Mapping(target = "unloading", ignore = true)
    @Mapping(target = "difference", source = "diff")
    @Mapping(target = "unloadingDate", ignore = true)
    BillDetailEntity toBillDetailEntity(BillWriteReq.BillDetailReq req);

    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "truck", ignore = true)
    @Mapping(target = "unloading", ignore = true)
    @Mapping(target = "difference", source = "diff")
    @Mapping(target = "unloadingDate", ignore = true)
    BillDetailEntity toBillDetailEntityForUpdate(BillWriteReq.BillDetailReq req);

    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "truck", source = "loading.truck")
    @Mapping(target = "truckNum", source = "loading.truckNumber")
    @Mapping(target = "lrDate", source = "loading.lrDate", qualifiedByName = "toLocalDate")
    @Mapping(target = "lrNum", source = "loading.lrNumber")
    @Mapping(target = "fromName", source = "loading.loadingDetails.fromName")
    @Mapping(target = "toName", source = "loading.loadingDetails.toName")
    @Mapping(target = "grade", source = "loading.loadingMaterial.materialName")
    @Mapping(target = "weight", source = "loading.loadingMaterial.loadedWeight")
    @Mapping(target = "unloadedWeight", source = "unloading.unloadedWeight")
    @Mapping(target = "difference", source = "unloading.changeInWeight")
    @Mapping(target = "freight", source = "loading.freightRate")
    @Mapping(target = "amount", source = "loading.freightAmount")
    @Mapping(target = "unloadingDate", source = "unloading.unloadingDate", qualifiedByName = "toLocalDate")
    @Mapping(target = "unloading", source = "unloading")
    BillDetailEntity toBillDetailEntityFromLoadingAndUnloading(LoadingEntity loading, UnloadingEntity unloading);

    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "truck", source = "loading.truck")
    @Mapping(target = "truckNum", source = "loading.truckNumber")
    @Mapping(target = "lrDate", source = "loading.lrDate", qualifiedByName = "toLocalDate")
    @Mapping(target = "lrNum", source = "loading.lrNumber")
    @Mapping(target = "fromName", source = "loading.loadingDetails.fromName")
    @Mapping(target = "toName", source = "loading.loadingDetails.toName")
    @Mapping(target = "grade", source = "loading.loadingMaterial.materialName")
    @Mapping(target = "weight", source = "loading.loadingMaterial.loadedWeight")
    @Mapping(target = "unloadedWeight", source = "unloading.unloadedWeight")
    @Mapping(target = "difference", source = "unloading.changeInWeight")
    @Mapping(target = "freight", source = "loading.freightRate")
    @Mapping(target = "amount", source = "loading.freightAmount")
    @Mapping(target = "unloadingDate", source = "unloading.unloadingDate", qualifiedByName = "toLocalDate")
    @Mapping(target = "unloading", source = "unloading")
    void updateBillDetailEntityFromLoadingAndUnloading(LoadingEntity loading, UnloadingEntity unloading,
                                                       @MappingTarget BillDetailEntity detail);

    @Named("toLocalDate")
    default LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
