package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.SeatDAO;
import com.railway.model.CoachSeat;
import com.railway.util.DBConnection;

public class SeatDAOImpl implements SeatDAO {

    @Override
    public boolean addSeat(CoachSeat seat) {
        String sql = "INSERT INTO coach_seats (coach_id, seat_number, berth_type, is_active) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seat.getCoachId());
            pstmt.setInt(2, seat.getSeatNumber());
            pstmt.setString(3, seat.getBerthType());
            pstmt.setBoolean(4, seat.isActive());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[SeatDAO] addSeat error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean addSeatsBatch(List<CoachSeat> seats) {
        String sql = "INSERT INTO coach_seats (coach_id, seat_number, berth_type, is_active) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (CoachSeat s : seats) {
                pstmt.setInt(1, s.getCoachId());
                pstmt.setInt(2, s.getSeatNumber());
                pstmt.setString(3, s.getBerthType());
                pstmt.setBoolean(4, s.isActive());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.err.println("[SeatDAO] addSeatsBatch error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateSeat(CoachSeat seat) {
        String sql = "UPDATE coach_seats SET berth_type = ?, is_active = ? WHERE seat_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, seat.getBerthType());
            pstmt.setBoolean(2, seat.isActive());
            pstmt.setInt(3, seat.getSeatId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[SeatDAO] updateSeat error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public CoachSeat getSeatById(int seatId) {
        String sql = "SELECT s.seat_id, s.coach_id, s.seat_number, s.berth_type, s.is_active, s.created_at, s.updated_at, " +
                     "c.coach_number, ct.coach_name " +
                     "FROM coach_seats s " +
                     "JOIN train_coaches c ON s.coach_id = c.coach_id " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "WHERE s.seat_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSeat(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeatDAO] getSeatById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<CoachSeat> getSeatsByCoachId(int coachId) {
        List<CoachSeat> list = new ArrayList<>();
        String sql = "SELECT s.seat_id, s.coach_id, s.seat_number, s.berth_type, s.is_active, s.created_at, s.updated_at, " +
                     "c.coach_number, ct.coach_name " +
                     "FROM coach_seats s " +
                     "JOIN train_coaches c ON s.coach_id = c.coach_id " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "WHERE s.coach_id = ? ORDER BY s.seat_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, coachId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSeat(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeatDAO] getSeatsByCoachId error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<CoachSeat> getAvailableSeatsForCoach(int coachId, Date journeyDate) {
        List<CoachSeat> list = new ArrayList<>();
        String sql = "SELECT s.seat_id, s.coach_id, s.seat_number, s.berth_type, s.is_active, s.created_at, s.updated_at, " +
                     "c.coach_number, ct.coach_name " +
                     "FROM coach_seats s " +
                     "JOIN train_coaches c ON s.coach_id = c.coach_id " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "WHERE s.coach_id = ? AND s.is_active = true " +
                     "  AND s.seat_id NOT IN (" +
                     "    SELECT bp.seat_id FROM booking_passengers bp " +
                     "    JOIN bookings b ON bp.booking_id = b.booking_id " +
                     "    JOIN booking_status bs ON bp.booking_status_id = bs.booking_status_id " +
                     "    WHERE b.journey_date = ? AND bs.status_name != 'CANCELLED' AND bp.seat_id IS NOT NULL" +
                     "  ) ORDER BY s.seat_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, coachId);
            pstmt.setDate(2, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSeat(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeatDAO] getAvailableSeatsForCoach error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<CoachSeat> getAvailableSeatsForTrainAndType(int trainId, int coachTypeId, Date journeyDate) {
        List<CoachSeat> list = new ArrayList<>();
        String sql = "SELECT s.seat_id, s.coach_id, s.seat_number, s.berth_type, s.is_active, s.created_at, s.updated_at, " +
                     "c.coach_number, ct.coach_name " +
                     "FROM coach_seats s " +
                     "JOIN train_coaches c ON s.coach_id = c.coach_id " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "WHERE c.train_id = ? AND c.coach_type_id = ? AND c.is_active = true AND s.is_active = true " +
                     "  AND s.seat_id NOT IN (" +
                     "    SELECT bp.seat_id FROM booking_passengers bp " +
                     "    JOIN bookings b ON bp.booking_id = b.booking_id " +
                     "    JOIN booking_status bs ON bp.booking_status_id = bs.booking_status_id " +
                     "    WHERE b.train_id = ? AND b.journey_date = ? AND bs.status_name != 'CANCELLED' AND bp.seat_id IS NOT NULL" +
                     "  ) ORDER BY c.coach_number, s.seat_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, coachTypeId);
            pstmt.setInt(3, trainId);
            pstmt.setDate(4, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSeat(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeatDAO] getAvailableSeatsForTrainAndType error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Integer> getBookedSeatIds(int trainId, Date journeyDate) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT bp.seat_id FROM booking_passengers bp " +
                     "JOIN bookings b ON bp.booking_id = b.booking_id " +
                     "JOIN booking_status bs ON bp.booking_status_id = bs.booking_status_id " +
                     "WHERE b.train_id = ? AND b.journey_date = ? AND bs.status_name != 'CANCELLED' AND bp.seat_id IS NOT NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setDate(2, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getInt("seat_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeatDAO] getBookedSeatIds error: " + e.getMessage());
        }
        return list;
    }

    private CoachSeat mapResultSetToSeat(ResultSet rs) throws SQLException {
        CoachSeat s = new CoachSeat();
        s.setSeatId(rs.getInt("seat_id"));
        s.setCoachId(rs.getInt("coach_id"));
        s.setSeatNumber(rs.getInt("seat_number"));
        s.setBerthType(rs.getString("berth_type"));
        s.setActive(rs.getBoolean("is_active"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        s.setUpdatedAt(rs.getTimestamp("updated_at"));
        s.setCoachNumber(rs.getString("coach_number"));
        s.setCoachTypeName(rs.getString("coach_name"));
        return s;
    }
}
