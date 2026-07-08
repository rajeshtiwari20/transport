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
        boolean fetchAll = req.getPageSize() != null && req.getPageSize() == -1;

        if (fetchAll) {
            List<TankerWiseReportEntity> tankerWiseReportEntities = findAllEntities(req);
            List<TankerWiseReportResp> content = reportMapper.toTankerWiseReportRespFromTankerWiseReportEntity(tankerWiseReportEntities);
            int total = content.size();
            Pageable pageable = PageRequest.of(0, Math.max(total, 1));
            return new PageImpl<>(content, pageable, total);
        }

        int pageNo = req.getPageNo() != null ? req.getPageNo() : 1;
        int pageSize = req.getPageSize() != null ? req.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        return findEntities(req, pageable).map(reportMapper::toTankerWiseReportResp);
    }

    private List<TankerWiseReportEntity> findAllEntities(ReportReq req) {
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();

        if (req.getTruckNumber() != null) {
            return tankerWiseReportRepository.findByUnloadingDateBetweenAndTruckNumber(startDate, endDate, req.getTruckNumber());
        }
        if (req.getPartyName() != null) {
            return tankerWiseReportRepository.findByUnloadingDateBetweenAndConsignorName(startDate, endDate, req.getPartyName());
        }
        return tankerWiseReportRepository.findByUnloadingDateBetween(startDate, endDate);
    }

    private Page<TankerWiseReportEntity> findEntities(ReportReq req, Pageable pageable) {
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();

        if (req.getTruckNumber() != null) {
            return tankerWiseReportRepository.findByUnloadingDateBetweenAndTruckNumber(startDate, endDate, req.getTruckNumber(), pageable);
        }
        if (req.getPartyName() != null) {
            return tankerWiseReportRepository.findByUnloadingDateBetweenAndConsignorName(startDate, endDate, req.getPartyName(), pageable);
        }
        return tankerWiseReportRepository.findByUnloadingDateBetween(startDate, endDate, pageable);
    }
}
