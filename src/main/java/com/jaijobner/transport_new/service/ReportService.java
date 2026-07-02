package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.report.ReportReq;
import com.jaijobner.transport_new.dto.report.TankerWiseReportResp;

import java.util.List;

public interface ReportService {
    List<TankerWiseReportResp> getTankerWiseReport(ReportReq req);
}
