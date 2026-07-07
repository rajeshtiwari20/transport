package com.jaijobner.transport_new.service.impl;

import com.jaijobner.transport_new.dto.report.ReportReq;
import com.jaijobner.transport_new.dto.report.TankerWiseReportResp;
import com.jaijobner.transport_new.entity.report.TankerWiseReportEntity;
import com.jaijobner.transport_new.mapper.ReportMapper;
import com.jaijobner.transport_new.repository.TankerWiseReportRepository;
import com.jaijobner.transport_new.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<TankerWiseReportResp> getTankerWiseReport(ReportReq req) {
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();
        boolean fetchAll = req.getPageSize() != null && req.getPageSize() == -1;

        if (fetchAll) {
            List<TankerWiseReportEntity> tankerWiseReportEntities;
            if (req.getTruckNumber() != null) {
                tankerWiseReportEntities = tankerWiseReportRepository.findByLrDateBetweenAndTruckNumber(startDate, endDate, req.getTruckNumber());
            } else {
                tankerWiseReportEntities = tankerWiseReportRepository.findByLrDateBetween(startDate, endDate);
            }
            List<TankerWiseReportResp> content = reportMapper.toTankerWiseReportRespFromTankerWiseReportEntity(tankerWiseReportEntities);
            int total = content.size();
            Pageable pageable = PageRequest.of(0, Math.max(total, 1));
            return new PageImpl<>(content, pageable, total);
        }

        int pageNo = req.getPageNo() != null ? req.getPageNo() : 1;
        int pageSize = req.getPageSize() != null ? req.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if (req.getTruckNumber() != null) {
            return tankerWiseReportRepository
                    .findByLrDateBetweenAndTruckNumber(startDate, endDate, req.getTruckNumber(), pageable)
                    .map(reportMapper::toTankerWiseReportResp);
        }
        return tankerWiseReportRepository
                .findByLrDateBetween(startDate, endDate, pageable)
                .map(reportMapper::toTankerWiseReportResp);
    }
}
