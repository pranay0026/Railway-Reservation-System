package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.CancellationDAO;
import com.railway.model.Cancellation;
import com.railway.model.RAC;
import com.railway.model.Waitlist;
import com.railway.util.DBConnection;

public class CancellationDAOImpl implements CancellationDAO {

    @Override
    public int recordCancellation(Connection conn, Cancellation cancellation) {
        String sql = "INSERT INTO cancellations (booking_id, cancelled_by, cancellation_reason, cancellation_charge, refund_amount) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, cancellation.getBookingId());
                pstmt.setInt(2, cancellation.getCancelledBy());
                pstmt.setString(3, cancellation.getCancellationReason());
                pstmt.setBigDecimal(4, cancellation.getCancellationCharge());
                pstmt.setBigDecimal(5, cancellation.getRefundAmount());

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
            System.err.println("[CancellationDAO] recordCancellation error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public Cancellation getCancellationByBookingId(int bookingId) {
        String sql = "SELECT c.cancellation_id, c.booking_id, c.cancelled_by, c.cancellation_date, c.cancellation_reason, " +
                     "c.cancellation_charge, c.refund_amount, c.created_at, c.updated_at, b.pnr_number, u.full_name " +
                     "FROM cancellations c " +
                     "JOIN bookings b ON c.booking_id = b.booking_id " +
                     "JOIN users u ON c.cancelled_by = u.user_id " +
                     "WHERE c.booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cancellation c = new Cancellation(
                            rs.getInt("cancellation_id"),
                            rs.getInt("booking_id"),
                            rs.getInt("cancelled_by"),
                            rs.getTimestamp("cancellation_date"),
                            rs.getString("cancellation_reason"),
                            rs.getBigDecimal("cancellation_charge"),
                            rs.getBigDecimal("refund_amount"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    c.setPnrNumber(rs.getString("pnr_number"));
                    c.setCancelledByUserName(rs.getString("full_name"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getCancellationByBookingId error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int addToWaitlist(Connection conn, Waitlist waitlist) {
        String sql = "INSERT INTO waitlist (passenger_id, booking_id, waitlist_number) VALUES (?, ?, ?)";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, waitlist.getPassengerId());
                pstmt.setInt(2, waitlist.getBookingId());
                pstmt.setInt(3, waitlist.getWaitlistNumber());

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
            System.err.println("[CancellationDAO] addToWaitlist error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public int addToRAC(Connection conn, RAC rac) {
        String sql = "INSERT INTO rac (passenger_id, booking_id, rac_number, seat_id) VALUES (?, ?, ?, ?)";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, rac.getPassengerId());
                pstmt.setInt(2, rac.getBookingId());
                pstmt.setInt(3, rac.getRacNumber());
                if (rac.getSeatId() != null && rac.getSeatId() > 0) {
                    pstmt.setInt(4, rac.getSeatId());
                } else {
                    pstmt.setNull(4, java.sql.Types.INTEGER);
                }

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
            System.err.println("[CancellationDAO] addToRAC error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public RAC getNextRACPassenger(Connection conn, int trainId, Date journeyDate) {
        String sql = "SELECT r.rac_id, r.passenger_id, r.booking_id, r.rac_number, r.seat_id, r.created_at, r.updated_at, " +
                     "bp.passenger_name, b.pnr_number " +
                     "FROM rac r " +
                     "JOIN bookings b ON r.booking_id = b.booking_id " +
                     "JOIN booking_passengers bp ON r.passenger_id = bp.passenger_id " +
                     "JOIN booking_status bs ON bp.booking_status_id = bs.booking_status_id " +
                     "WHERE b.train_id = ? AND b.journey_date = ? AND bs.status_name = 'RAC' " +
                     "ORDER BY r.rac_number ASC LIMIT 1";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, trainId);
                pstmt.setDate(2, journeyDate);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        RAC rac = new RAC(
                                rs.getInt("rac_id"),
                                rs.getInt("passenger_id"),
                                rs.getInt("booking_id"),
                                rs.getInt("rac_number"),
                                rs.getInt("seat_id"),
                                rs.getTimestamp("created_at"),
                                rs.getTimestamp("updated_at")
                        );
                        if (rs.wasNull()) {
                            rac.setSeatId(null);
                        }
                        rac.setPassengerName(rs.getString("passenger_name"));
                        rac.setPnrNumber(rs.getString("pnr_number"));
                        return rac;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getNextRACPassenger error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Waitlist getNextWaitlistPassenger(Connection conn, int trainId, Date journeyDate) {
        String sql = "SELECT w.waitlist_id, w.passenger_id, w.booking_id, w.waitlist_number, w.created_at, w.updated_at, " +
                     "bp.passenger_name, b.pnr_number " +
                     "FROM waitlist w " +
                     "JOIN bookings b ON w.booking_id = b.booking_id " +
                     "JOIN booking_passengers bp ON w.passenger_id = bp.passenger_id " +
                     "JOIN booking_status bs ON bp.booking_status_id = bs.booking_status_id " +
                     "WHERE b.train_id = ? AND b.journey_date = ? AND bs.status_name = 'WAITLIST' " +
                     "ORDER BY w.waitlist_number ASC LIMIT 1";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, trainId);
                pstmt.setDate(2, journeyDate);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Waitlist wl = new Waitlist(
                                rs.getInt("waitlist_id"),
                                rs.getInt("passenger_id"),
                                rs.getInt("booking_id"),
                                rs.getInt("waitlist_number"),
                                rs.getTimestamp("created_at"),
                                rs.getTimestamp("updated_at")
                        );
                        wl.setPassengerName(rs.getString("passenger_name"));
                        wl.setPnrNumber(rs.getString("pnr_number"));
                        return wl;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getNextWaitlistPassenger error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean removeRAC(Connection conn, int racId) {
        String sql = "DELETE FROM rac WHERE rac_id = ?";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, racId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] removeRAC error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeWaitlist(Connection conn, int waitlistId) {
        String sql = "DELETE FROM waitlist WHERE waitlist_id = ?";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, waitlistId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] removeWaitlist error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<RAC> getRACQueueForTrainAndDate(int trainId, Date journeyDate) {
        List<RAC> list = new ArrayList<>();
        String sql = "SELECT r.rac_id, r.passenger_id, r.booking_id, r.rac_number, r.seat_id, r.created_at, r.updated_at, " +
                     "bp.passenger_name, b.pnr_number " +
                     "FROM rac r " +
                     "JOIN bookings b ON r.booking_id = b.booking_id " +
                     "JOIN booking_passengers bp ON r.passenger_id = bp.passenger_id " +
                     "WHERE b.train_id = ? AND b.journey_date = ? ORDER BY r.rac_number ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setDate(2, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RAC rac = new RAC(
                            rs.getInt("rac_id"),
                            rs.getInt("passenger_id"),
                            rs.getInt("booking_id"),
                            rs.getInt("rac_number"),
                            rs.getInt("seat_id"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    rac.setPassengerName(rs.getString("passenger_name"));
                    rac.setPnrNumber(rs.getString("pnr_number"));
                    list.add(rac);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getRACQueueForTrainAndDate error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Waitlist> getWaitlistQueueForTrainAndDate(int trainId, Date journeyDate) {
        List<Waitlist> list = new ArrayList<>();
        String sql = "SELECT w.waitlist_id, w.passenger_id, w.booking_id, w.waitlist_number, w.created_at, w.updated_at, " +
                     "bp.passenger_name, b.pnr_number " +
                     "FROM waitlist w " +
                     "JOIN bookings b ON w.booking_id = b.booking_id " +
                     "JOIN booking_passengers bp ON w.passenger_id = bp.passenger_id " +
                     "WHERE b.train_id = ? AND b.journey_date = ? ORDER BY w.waitlist_number ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setDate(2, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Waitlist wl = new Waitlist(
                            rs.getInt("waitlist_id"),
                            rs.getInt("passenger_id"),
                            rs.getInt("booking_id"),
                            rs.getInt("waitlist_number"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    wl.setPassengerName(rs.getString("passenger_name"));
                    wl.setPnrNumber(rs.getString("pnr_number"));
                    list.add(wl);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getWaitlistQueueForTrainAndDate error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int getNextRACNumber(Connection conn, int trainId, Date journeyDate) {
        String sql = "SELECT COALESCE(MAX(r.rac_number), 0) + 1 AS next_num " +
                     "FROM rac r JOIN bookings b ON r.booking_id = b.booking_id " +
                     "WHERE b.train_id = ? AND b.journey_date = ?";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, trainId);
                pstmt.setDate(2, journeyDate);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("next_num");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getNextRACNumber error: " + e.getMessage());
        }
        return 1;
    }

    @Override
    public int getNextWaitlistNumber(Connection conn, int trainId, Date journeyDate) {
        String sql = "SELECT COALESCE(MAX(w.waitlist_number), 0) + 1 AS next_num " +
                     "FROM waitlist w JOIN bookings b ON w.booking_id = b.booking_id " +
                     "WHERE b.train_id = ? AND b.journey_date = ?";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, trainId);
                pstmt.setDate(2, journeyDate);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("next_num");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getNextWaitlistNumber error: " + e.getMessage());
        }
        return 1;
    }

    @Override
    public List<Cancellation> getAllCancellations() {
        List<Cancellation> list = new ArrayList<>();
        String sql = "SELECT c.cancellation_id, c.booking_id, c.cancelled_by, c.cancellation_date, c.cancellation_reason, " +
                     "c.cancellation_charge, c.refund_amount, c.created_at, c.updated_at, b.pnr_number, u.full_name " +
                     "FROM cancellations c " +
                     "JOIN bookings b ON c.booking_id = b.booking_id " +
                     "JOIN users u ON c.cancelled_by = u.user_id " +
                     "ORDER BY c.cancellation_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Cancellation c = new Cancellation(
                        rs.getInt("cancellation_id"),
                        rs.getInt("booking_id"),
                        rs.getInt("cancelled_by"),
                        rs.getTimestamp("cancellation_date"),
                        rs.getString("cancellation_reason"),
                        rs.getBigDecimal("cancellation_charge"),
                        rs.getBigDecimal("refund_amount"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                c.setPnrNumber(rs.getString("pnr_number"));
                c.setCancelledByUserName(rs.getString("full_name"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("[CancellationDAO] getAllCancellations error: " + e.getMessage());
        }
        return list;
    }
}
