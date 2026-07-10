package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.bill.BillGetResp;
import com.jaijobner.transport_new.dto.bill.BillResp;
import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.BillDetailEntity;
import com.jaijobner.transport_new.entity.BillEntity;
import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.entity.UnloadingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface BillMapper {
    @Mapping(target = "unloadingId", source = "unloading.id")
    @Mapping(target = "total", source = "totalAmount")
    @Mapping(target = "shortage", expression = "java(bill.getIsShortage() != null && bill.getIsShortage() ? 1.0 : 0.0)")
    BillResp toBillResp(BillEntity bill);

    @Mapping(target = "unloadingId", source = "unloading.id")
    @Mapping(target = "billDetailList", source = "billDetails")
    @Mapping(target = "total", source = "totalAmount")
    @Mapping(target = "shortage", expression = "java(bill.getIsShortage() != null && bill.getIsShortage() ? 1.0 : 0.0)")
    BillGetResp toBillGetResp(BillEntity bill);

    @Mapping(target = "diff", source = "difference")
    BillGetResp.BillDetailGetResp toBillDetailGetResp(BillDetailEntity detail);

    @Mapping(target = "unloading", ignore = true)
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
    @Mapping(target = "isShortage", expression = "java(req.getShortage() != null && req.getShortage() != 0)")
    @Mapping(target = "totalAmount", source = "total")
    BillEntity toBillEntity(BillWriteReq req);

    @Mapping(target = "unloading", ignore = true)
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
    @Mapping(target = "isShortage", expression = "java(req.getShortage() != null && req.getShortage() != 0)")
    @Mapping(target = "totalAmount", source = "total")
    void updateBillEntity(BillWriteReq req, @MappingTarget BillEntity bill);

    @Mapping(target = "unloading", source = "unloading")
    @Mapping(target = "consignee", source = "loading.consignee")
    @Mapping(target = "billDetails", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BillEntity toBillEntityFromLoadingAndUnloading(LoadingEntity loading, UnloadingEntity unloading);
}
