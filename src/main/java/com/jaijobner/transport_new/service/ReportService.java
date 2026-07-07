package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.report.ReportReq;
import com.jaijobner.transport_new.dto.report.TankerWiseReportResp;
import org.springframework.data.domain.Page;

public interface ReportService {
    Page<TankerWiseReportResp> getTankerWiseReport(ReportReq req);
}
