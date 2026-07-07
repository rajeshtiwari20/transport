package com.jaijobner.transport_new.mapper;

import com.jaijobner.transport_new.dto.expense.ExpenseCreateReq;
import com.jaijobner.transport_new.dto.expense.ExpenseResp;
import com.jaijobner.transport_new.dto.expense.ExpenseUpdateReq;
import com.jaijobner.transport_new.entity.ExpenseEntity;
import com.jaijobner.transport_new.entity.TruckEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(target = "truckId", source = "truck.id")
    ExpenseResp expenseEntityToExpenseResp(ExpenseEntity expense);

    @Mapping(target = "truck", source = "truck")
    @Mapping(target = "truckNumber", source = "truck.truckNum")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ExpenseEntity expenseCreateReqToExpenseEntity(ExpenseCreateReq req, TruckEntity truck);

    @Mapping(target = "truck", source = "truck")
    @Mapping(target = "truckNumber", source = "truck.truckNum")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateExpenseEntityFromUpdateReq(ExpenseUpdateReq req, TruckEntity truck, @MappingTarget ExpenseEntity expense);
}
