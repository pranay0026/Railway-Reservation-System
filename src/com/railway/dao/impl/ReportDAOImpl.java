package com.railway.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.railway.dao.ReportDAO;
import com.railway.util.DBConnection;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public Map<String, Object> getBookingSummaryReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        String sql = "SELECT " +
                     "  COUNT(*) AS total_bookings, " +
                     "  SUM(CASE WHEN bs.status_name = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_count, " +
                     "  SUM(CASE WHEN bs.status_name = 'RAC' THEN 1 ELSE 0 END) AS rac_count, " +
                     "  SUM(CASE WHEN bs.status_name = 'WAITLIST' THEN 1 ELSE 0 END) AS waitlist_count, " +
                     "  SUM(CASE WHEN bs.status_name = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_count " +
                     "FROM bookings b " +
                     "JOIN booking_status bs ON b.booking_status_id = bs.booking_status_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                report.put("total_bookings", rs.getInt("total_bookings"));
                report.put("confirmed_count", rs.getInt("confirmed_count"));
                report.put("rac_count", rs.getInt("rac_count"));
                report.put("waitlist_count", rs.getInt("waitlist_count"));
                report.put("cancelled_count", rs.getInt("cancelled_count"));
            }
        } catch (SQLException e) {
            System.err.println("[ReportDAO] getBookingSummaryReport error: " + e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> getRevenueReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        String sql = "SELECT " +
                     "  (SELECT COALESCE(SUM(amount), 0) FROM payments WHERE payment_status_id = (SELECT payment_status_id FROM payment_status WHERE status_name = 'SUCCESS')) AS total_revenue, " +
                     "  (SELECT COALESCE(SUM(refund_amount), 0) FROM refunds WHERE refund_status IN ('PROCESSED', 'PENDING')) AS total_refunds, " +
                     "  (SELECT COALESCE(SUM(cancellation_charge), 0) FROM cancellations) AS total_cancellation_charges";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                BigDecimal totalRev = rs.getBigDecimal("total_revenue");
                BigDecimal totalRef = rs.getBigDecimal("total_refunds");
                BigDecimal totalCharges = rs.getBigDecimal("total_cancellation_charges");
                BigDecimal netRevenue = totalRev.subtract(totalRef);

                report.put("total_revenue", totalRev);
                report.put("total_refunds", totalRef);
                report.put("cancellation_charges", totalCharges);
                report.put("net_revenue", netRevenue);
            }
        } catch (SQLException e) {
            System.err.println("[ReportDAO] getRevenueReport error: " + e.getMessage());
        }
        return report;
    }

    @Override
    public List<Map<String, Object>> getTrainUsageReport() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.train_number, t.train_name, " +
                     "  COUNT(b.booking_id) AS total_bookings, " +
                     "  COALESCE(SUM(b.total_passengers), 0) AS total_passengers, " +
                     "  COALESCE(SUM(CASE WHEN bs.status_name != 'CANCELLED' THEN b.total_fare ELSE 0 END), 0) AS train_revenue " +
                     "FROM trains t " +
                     "LEFT JOIN bookings b ON t.train_id = b.train_id " +
                     "LEFT JOIN booking_status bs ON b.booking_status_id = bs.booking_status_id " +
                     "GROUP BY t.train_id, t.train_number, t.train_name " +
                     "ORDER BY total_bookings DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("train_number", rs.getString("train_number"));
                map.put("train_name", rs.getString("train_name"));
                map.put("total_bookings", rs.getInt("total_bookings"));
                map.put("total_passengers", rs.getInt("total_passengers"));
                map.put("revenue", rs.getBigDecimal("train_revenue"));
                list.add(map);
            }
        } catch (SQLException e) {
            System.err.println("[ReportDAO] getTrainUsageReport error: " + e.getMessage());
        }
        return list;
    }
}
