package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.bill.BillGetResp;
import com.jaijobner.transport_new.dto.bill.BillReq;
import com.jaijobner.transport_new.dto.bill.BillResp;
import com.jaijobner.transport_new.dto.bill.BillWriteReq;
import com.jaijobner.transport_new.entity.LoadingEntity;
import com.jaijobner.transport_new.entity.UnloadingEntity;
import org.springframework.data.domain.Page;

public interface BillService {
    Page<BillResp> getBills(BillReq req);

    void createBill(LoadingEntity loading, UnloadingEntity unloading);

    void updateBill(LoadingEntity loading, UnloadingEntity unloading);

    void createBill(BillWriteReq req);

    BillGetResp getBill(Long id);

    void updateBill(Long id, BillWriteReq req);
}
