package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.loading.LoadingCreateReq;
import com.jaijobner.transport_new.dto.loading.LoadingGetResp;
import com.jaijobner.transport_new.dto.loading.LoadingReq;
import com.jaijobner.transport_new.dto.loading.LoadingResp;
import org.springframework.data.domain.Page;

public interface LoadingService {
    Page<LoadingResp> getLoadings(LoadingReq req);

    void createLoading(LoadingCreateReq req);

    LoadingGetResp getLoading(Long id);
}
