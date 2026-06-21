package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.loading.*;
import org.springframework.data.domain.Page;

public interface LoadingService {
    Page<LoadingResp> getLoadings(LoadingReq req);

    void createLoading(LoadingCreateReq req);

    LoadingGetResp getLoading(Long id);

    LoadingLRNumResp getNewLrNum(Long companyId);

    void updateLoading(Long id, LoadingUpdateReq req);
}
