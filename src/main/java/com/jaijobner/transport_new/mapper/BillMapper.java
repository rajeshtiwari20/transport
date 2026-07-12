package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.bill.BillGetResp;
import com.jaijobner.transport_new.dto.bill.BillResp;
import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.BillDetailEntity;
import com.jaijobner.transport_new.entity.BillEntity;
import com.jaijobner.transport_new.entity.LoadingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PartyMapper.class)
public interface BillMapper {
    @Mapping(target = "total", source = "totalAmount")
    @Mapping(target = "shortage", expression = "java(bill.getIsShortage() != null && bill.getIsShortage() ? 1.0 : 0.0)")
    @Mapping(target = "partyName", source = "consignee.partyName")
    @Mapping(target = "year", expression = "java(bill.getYear() != null ? bill.getYear().getValue() : null)")
    @Mapping(target = "month", expression = "java(bill.getMonth() != null ? java.time.Month.of(bill.getMonth()).name().charAt(0) + java.time.Month.of(bill.getMonth()).name().substring(1).toLowerCase() : null)")
    BillResp toBillResp(BillEntity bill);

    @Mapping(target = "billDetailList", source = "billDetails")
    @Mapping(target = "total", source = "totalAmount")
    @Mapping(target = "shortage", expression = "java(bill.getIsShortage() != null && bill.getIsShortage() ? 1.0 : 0.0)")
    @Mapping(target = "year", expression = "java(bill.getYear() != null ? bill.getYear().getValue() : null)")
    @Mapping(target = "consignee", source = "consignee")
    BillGetResp toBillGetResp(BillEntity bill);

    @Mapping(target = "diff", source = "difference")
    @Mapping(target = "truckId", source = "truck.id")
    @Mapping(target = "unloadingId", source = "unloading.id")
    BillGetResp.BillDetailGetResp toBillDetailGetResp(BillDetailEntity detail);

    @Mapping(target = "billDetails", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "month", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "consignee", ignore = true)
    @Mapping(target = "variationRate", ignore = true)
    @Mapping(target = "variationWeight", ignore = true)
    @Mapping(target = "isShortage", expression = "java(req.getShortage() != null && req.getShortage() != 0)")
    @Mapping(target = "totalAmount", source = "total")
    BillEntity toBillEntity(BillWriteReq req);

    @Mapping(target = "billDetails", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "month", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "consignee", ignore = true)
    @Mapping(target = "variationRate", ignore = true)
    @Mapping(target = "variationWeight", ignore = true)
    @Mapping(target = "isShortage", expression = "java(req.getShortage() != null && req.getShortage() != 0)")
    @Mapping(target = "totalAmount", source = "total")
    void updateBillEntity(BillWriteReq req, @MappingTarget BillEntity bill);

    @Mapping(target = "consignee", source = "loading.consignee")
    @Mapping(target = "billDetails", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BillEntity toBillEntityFromLoading(LoadingEntity loading);
}
