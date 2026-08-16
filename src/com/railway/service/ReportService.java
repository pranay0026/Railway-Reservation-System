package com.railway.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String, Object> getBookingSummary();
    Map<String, Object> getRevenueSummary();
    List<Map<String, Object>> getTrainUsage();
}
