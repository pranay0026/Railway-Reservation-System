package com.railway.dao;

import java.util.List;
import java.util.Map;

public interface ReportDAO {
    Map<String, Object> getBookingSummaryReport();
    Map<String, Object> getRevenueReport();
    List<Map<String, Object>> getTrainUsageReport();
}
