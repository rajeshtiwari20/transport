package com.jaijobner.transport_new.service.impl;

import com.jaijobner.transport_new.dto.report.ReportReq;
import com.jaijobner.transport_new.dto.report.TankerWiseReportResp;
import com.jaijobner.transport_new.entity.report.TankerWiseReportEntity;
import com.jaijobner.transport_new.mapper.ReportMapper;
import com.jaijobner.transport_new.repository.TankerWiseReportRepository;
import com.jaijobner.transport_new.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final TankerWiseReportRepository tankerWiseReportRepository;
    private final ReportMapper reportMapper;

    @Override
    public List<TankerWiseReportResp> getTankerWiseReport(ReportReq req) {
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();
        List<TankerWiseReportEntity> tankerWiseReportEntities;
        boolean fetchAll = req.getPageSize() != null && req.getPageSize() == -1;

        if (fetchAll) {
            if (req.getTruckNumber() != null) {
                tankerWiseReportEntities = tankerWiseReportRepository.findByLrDateBetweenAndTruckNumber(startDate, endDate, req.getTruckNumber());
            } else {
                tankerWiseReportEntities = tankerWiseReportRepository.findByLrDateBetween(startDate, endDate);
            }
        } else {
            int pageNo = req.getPageNo() != null ? req.getPageNo() : 1;
            int pageSize = req.getPageSize() != null ? req.getPageSize() : 10;
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

            if (req.getTruckNumber() != null) {
                tankerWiseReportEntities = tankerWiseReportRepository
                        .findByLrDateBetweenAndTruckNumber(startDate, endDate, req.getTruckNumber(), pageable)
                        .getContent();
            } else {
                tankerWiseReportEntities = tankerWiseReportRepository
                        .findByLrDateBetween(startDate, endDate, pageable)
                        .getContent();
            }
        }
        return reportMapper.toTankerWiseReportRespFromTankerWiseReportEntity(tankerWiseReportEntities);
    }
}
