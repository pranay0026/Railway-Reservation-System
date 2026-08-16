package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.PaymentDAO;
import com.railway.model.Payment;
import com.railway.model.Refund;
import com.railway.util.DBConnection;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public int recordPayment(Connection conn, Payment payment) {
        String sql = "INSERT INTO payments (booking_id, payment_method_id, payment_status_id, transaction_id, amount) VALUES (?, ?, ?, ?, ?)";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, payment.getBookingId());
                pstmt.setInt(2, payment.getPaymentMethodId());
                pstmt.setInt(3, payment.getPaymentStatusId());
                pstmt.setString(4, payment.getTransactionId());
                pstmt.setBigDecimal(5, payment.getAmount());

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] recordPayment error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public Payment getPaymentByBookingId(int bookingId) {
        String sql = "SELECT p.payment_id, p.booking_id, p.payment_method_id, p.payment_status_id, p.transaction_id, " +
                     "p.amount, p.payment_date, p.created_at, p.updated_at, pm.method_name, ps.status_name " +
                     "FROM payments p " +
                     "JOIN payment_method pm ON p.payment_method_id = pm.payment_method_id " +
                     "JOIN payment_status ps ON p.payment_status_id = ps.payment_status_id " +
                     "WHERE p.booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] getPaymentByBookingId error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT p.payment_id, p.booking_id, p.payment_method_id, p.payment_status_id, p.transaction_id, " +
                     "p.amount, p.payment_date, p.created_at, p.updated_at, pm.method_name, ps.status_name " +
                     "FROM payments p " +
                     "JOIN payment_method pm ON p.payment_method_id = pm.payment_method_id " +
                     "JOIN payment_status ps ON p.payment_status_id = ps.payment_status_id " +
                     "WHERE p.payment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] getPaymentById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int recordRefund(Connection conn, Refund refund) {
        String sql = "INSERT INTO refunds (payment_id, refund_amount, refund_reason, refund_status, refund_date) " +
                     "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, refund.getPaymentId());
                pstmt.setBigDecimal(2, refund.getRefundAmount());
                pstmt.setString(3, refund.getRefundReason());
                pstmt.setString(4, refund.getRefundStatus() != null ? refund.getRefundStatus() : "PROCESSED");

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] recordRefund error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public Refund getRefundByPaymentId(int paymentId) {
        String sql = "SELECT r.refund_id, r.payment_id, r.refund_amount, r.refund_reason, r.refund_status, r.refund_date, " +
                     "r.created_at, r.updated_at, p.transaction_id, b.pnr_number " +
                     "FROM refunds r " +
                     "JOIN payments p ON r.payment_id = p.payment_id " +
                     "JOIN bookings b ON p.booking_id = b.booking_id " +
                     "WHERE r.payment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Refund ref = new Refund(
                            rs.getInt("refund_id"),
                            rs.getInt("payment_id"),
                            rs.getBigDecimal("refund_amount"),
                            rs.getString("refund_reason"),
                            rs.getString("refund_status"),
                            rs.getTimestamp("refund_date"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    ref.setTransactionId(rs.getString("transaction_id"));
                    ref.setPnrNumber(rs.getString("pnr_number"));
                    return ref;
                }
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] getRefundByPaymentId error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.payment_id, p.booking_id, p.payment_method_id, p.payment_status_id, p.transaction_id, " +
                     "p.amount, p.payment_date, p.created_at, p.updated_at, pm.method_name, ps.status_name " +
                     "FROM payments p " +
                     "JOIN payment_method pm ON p.payment_method_id = pm.payment_method_id " +
                     "JOIN payment_status ps ON p.payment_status_id = ps.payment_status_id " +
                     "ORDER BY p.payment_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] getAllPayments error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Refund> getAllRefunds() {
        List<Refund> list = new ArrayList<>();
        String sql = "SELECT r.refund_id, r.payment_id, r.refund_amount, r.refund_reason, r.refund_status, r.refund_date, " +
                     "r.created_at, r.updated_at, p.transaction_id, b.pnr_number " +
                     "FROM refunds r " +
                     "JOIN payments p ON r.payment_id = p.payment_id " +
                     "JOIN bookings b ON p.booking_id = b.booking_id " +
                     "ORDER BY r.refund_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Refund ref = new Refund(
                        rs.getInt("refund_id"),
                        rs.getInt("payment_id"),
                        rs.getBigDecimal("refund_amount"),
                        rs.getString("refund_reason"),
                        rs.getString("refund_status"),
                        rs.getTimestamp("refund_date"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                ref.setTransactionId(rs.getString("transaction_id"));
                ref.setPnrNumber(rs.getString("pnr_number"));
                list.add(ref);
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] getAllRefunds error: " + e.getMessage());
        }
        return list;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setBookingId(rs.getInt("booking_id"));
        p.setPaymentMethodId(rs.getInt("payment_method_id"));
        p.setPaymentStatusId(rs.getInt("payment_status_id"));
        p.setTransactionId(rs.getString("transaction_id"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentDate(rs.getTimestamp("payment_date"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        p.setPaymentMethodName(rs.getString("method_name"));
        p.setPaymentStatusName(rs.getString("status_name"));
        return p;
    }
}
