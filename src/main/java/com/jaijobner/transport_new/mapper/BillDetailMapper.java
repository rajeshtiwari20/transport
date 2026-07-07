package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.BillDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillDetailMapper {
    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BillDetailEntity toBillDetailEntity(BillWriteReq.BillDetailReq req);

    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BillDetailEntity toBillDetailEntityForUpdate(BillWriteReq.BillDetailReq req);
}
