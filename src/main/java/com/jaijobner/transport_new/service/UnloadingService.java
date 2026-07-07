package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.unloading.UnloadingGetResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingReq;
import com.jaijobner.transport_new.dto.unloading.UnloadingResp;
import com.jaijobner.transport_new.dto.unloading.UnloadingWriteReq;
import org.springframework.data.domain.Page;

public interface UnloadingService {
    Page<UnloadingResp> getUnloadings(UnloadingReq req);

    Page<UnloadingResp> getNonBilledUnloadings(UnloadingReq req);

    void createUnloading(UnloadingWriteReq req);

    UnloadingGetResp getUnloading(Long id);

    void updateUnloading(Long id, UnloadingWriteReq req);
}
