package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.bill.BillGetResp;
import com.jaijobner.transport_new.dto.bill.BillResp;
import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.BillDetailEntity;
import com.jaijobner.transport_new.entity.BillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BillMapper {
    @Mapping(target = "unloadingId", source = "unloading.id")
    BillResp toBillResp(BillEntity bill);

    @Mapping(target = "unloadingId", source = "unloading.id")
    @Mapping(target = "billDetailList", source = "billDetails")
    BillGetResp toBillGetResp(BillEntity bill);

    BillGetResp.BillDetailGetResp toBillDetailGetResp(BillDetailEntity detail);

    @Mapping(target = "unloading", ignore = true)
    @Mapping(target = "billDetails", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BillEntity toBillEntity(BillWriteReq req);

    @Mapping(target = "unloading", ignore = true)
    @Mapping(target = "billDetails", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateBillEntity(BillWriteReq req, @MappingTarget BillEntity bill);
}
