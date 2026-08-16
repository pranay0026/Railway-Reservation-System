package com.railway.service.impl;

import java.util.List;
import java.util.Map;
import com.railway.dao.ReportDAO;
import com.railway.dao.impl.ReportDAOImpl;
import com.railway.service.ReportService;

public class ReportServiceImpl implements ReportService {
    private final ReportDAO reportDAO;

    public ReportServiceImpl() {
        this.reportDAO = new ReportDAOImpl();
    }

    @Override
    public Map<String, Object> getBookingSummary() {
        return reportDAO.getBookingSummaryReport();
    }

    @Override
    public Map<String, Object> getRevenueSummary() {
        return reportDAO.getRevenueReport();
    }

    @Override
    public List<Map<String, Object>> getTrainUsage() {
        return reportDAO.getTrainUsageReport();
    }
}
