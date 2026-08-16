package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.AvailabilityDAO;
import com.railway.model.TrainAvailability;
import com.railway.util.DBConnection;

public class AvailabilityDAOImpl implements AvailabilityDAO {

    @Override
    public TrainAvailability getAvailability(int trainId, int coachId, Date journeyDate) {
        String sql = "SELECT a.availability_id, a.train_id, a.coach_id, a.journey_date, a.available_seats, a.rac_count, a.waiting_count, " +
                     "a.created_at, a.updated_at, c.coach_number, ct.coach_name, ct.coach_type_id, t.train_number, t.train_name " +
                     "FROM train_availability a " +
                     "JOIN train_coaches c ON a.coach_id = c.coach_id " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "JOIN trains t ON a.train_id = t.train_id " +
                     "WHERE a.train_id = ? AND a.coach_id = ? AND a.journey_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, coachId);
            pstmt.setDate(3, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAvailability(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AvailabilityDAO] getAvailability error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<TrainAvailability> getAvailabilityForTrain(int trainId, Date journeyDate) {
        List<TrainAvailability> list = new ArrayList<>();
        String sql = "SELECT a.availability_id, a.train_id, a.coach_id, a.journey_date, a.available_seats, a.rac_count, a.waiting_count, " +
                     "a.created_at, a.updated_at, c.coach_number, ct.coach_name, ct.coach_type_id, t.train_number, t.train_name " +
                     "FROM train_availability a " +
                     "JOIN train_coaches c ON a.coach_id = c.coach_id " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "JOIN trains t ON a.train_id = t.train_id " +
                     "WHERE a.train_id = ? AND a.journey_date = ? ORDER BY c.coach_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setDate(2, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAvailability(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[AvailabilityDAO] getAvailabilityForTrain error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean initializeAvailabilityIfAbsent(Connection conn, int trainId, int coachId, Date journeyDate, int totalSeats) {
        String sql = "INSERT INTO train_availability (train_id, coach_id, journey_date, available_seats, rac_count, waiting_count) " +
                     "VALUES (?, ?, ?, ?, 0, 0) " +
                     "ON DUPLICATE KEY UPDATE availability_id = availability_id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, coachId);
            pstmt.setDate(3, journeyDate);
            pstmt.setInt(4, totalSeats);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[AvailabilityDAO] initializeAvailabilityIfAbsent error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateAvailability(Connection conn, int trainId, int coachId, Date journeyDate, int seatsDelta, int racDelta, int waitlistDelta) {
        String sql = "UPDATE train_availability SET " +
                     "available_seats = GREATEST(0, available_seats + ?), " +
                     "rac_count = GREATEST(0, rac_count + ?), " +
                     "waiting_count = GREATEST(0, waiting_count + ?) " +
                     "WHERE train_id = ? AND coach_id = ? AND journey_date = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatsDelta);
            pstmt.setInt(2, racDelta);
            pstmt.setInt(3, waitlistDelta);
            pstmt.setInt(4, trainId);
            pstmt.setInt(5, coachId);
            pstmt.setDate(6, journeyDate);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[AvailabilityDAO] updateAvailability error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int getTotalAvailableSeatsForClass(int trainId, int coachTypeId, Date journeyDate) {
        String sql = "SELECT COALESCE(SUM(a.available_seats), 0) AS total_avail " +
                     "FROM train_availability a " +
                     "JOIN train_coaches c ON a.coach_id = c.coach_id " +
                     "WHERE a.train_id = ? AND c.coach_type_id = ? AND a.journey_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, coachTypeId);
            pstmt.setDate(3, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_avail");
                }
            }
        } catch (SQLException e) {
            System.err.println("[AvailabilityDAO] getTotalAvailableSeatsForClass error: " + e.getMessage());
        }
        return 0;
    }

    private TrainAvailability mapResultSetToAvailability(ResultSet rs) throws SQLException {
        TrainAvailability a = new TrainAvailability();
        a.setAvailabilityId(rs.getInt("availability_id"));
        a.setTrainId(rs.getInt("train_id"));
        a.setCoachId(rs.getInt("coach_id"));
        a.setJourneyDate(rs.getDate("journey_date"));
        a.setAvailableSeats(rs.getInt("available_seats"));
        a.setRacCount(rs.getInt("rac_count"));
        a.setWaitingCount(rs.getInt("waiting_count"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        a.setUpdatedAt(rs.getTimestamp("updated_at"));
        a.setCoachNumber(rs.getString("coach_number"));
        a.setCoachTypeName(rs.getString("coach_name"));
        a.setCoachTypeId(rs.getInt("coach_type_id"));
        a.setTrainNumber(rs.getString("train_number"));
        a.setTrainName(rs.getString("train_name"));
        return a;
    }
}
