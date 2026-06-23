package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.loading.LoadingMaterialWeightResp;
import com.jaijobner.transport_new.dto.loading.LoadingResp;
import com.jaijobner.transport_new.dto.loading.LoadingReq;
import com.jaijobner.transport_new.dto.loading.LoadingCreateReq;
import com.jaijobner.transport_new.dto.loading.LoadingLRNumResp;
import com.jaijobner.transport_new.dto.loading.LoadingGetResp;
import com.jaijobner.transport_new.dto.loading.LoadingUpdateReq;
import com.jaijobner.transport_new.dto.loading.LoadingUnloadingResp;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LoadingService {
    Page<LoadingResp> getLoadings(LoadingReq req);

    void createLoading(LoadingCreateReq req);

    LoadingGetResp getLoading(Long id);

    LoadingLRNumResp getNewLrNum(Long companyId);

    void updateLoading(Long id, LoadingUpdateReq req);

    List<LoadingUnloadingResp> getUploadingList();

    LoadingMaterialWeightResp getLoadingMaterialWeight(Long id);
}
