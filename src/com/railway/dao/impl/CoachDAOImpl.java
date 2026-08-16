package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.CoachDAO;
import com.railway.model.TrainCoach;
import com.railway.util.DBConnection;

public class CoachDAOImpl implements CoachDAO {

    @Override
    public boolean addCoach(TrainCoach coach) {
        String sql = "INSERT INTO train_coaches (train_id, coach_type_id, coach_number, total_seats, is_active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, coach.getTrainId());
            pstmt.setInt(2, coach.getCoachTypeId());
            pstmt.setString(3, coach.getCoachNumber().trim());
            pstmt.setInt(4, coach.getTotalSeats());
            pstmt.setBoolean(5, coach.isActive());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[CoachDAO] addCoach error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateCoach(TrainCoach coach) {
        String sql = "UPDATE train_coaches SET coach_type_id = ?, coach_number = ?, total_seats = ?, is_active = ? WHERE coach_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, coach.getCoachTypeId());
            pstmt.setString(2, coach.getCoachNumber().trim());
            pstmt.setInt(3, coach.getTotalSeats());
            pstmt.setBoolean(4, coach.isActive());
            pstmt.setInt(5, coach.getCoachId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[CoachDAO] updateCoach error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean setCoachActive(int coachId, boolean active) {
        String sql = "UPDATE train_coaches SET is_active = ? WHERE coach_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, active);
            pstmt.setInt(2, coachId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[CoachDAO] setCoachActive error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public TrainCoach getCoachById(int coachId) {
        String sql = "SELECT c.coach_id, c.train_id, c.coach_type_id, c.coach_number, c.total_seats, c.is_active, c.created_at, c.updated_at, " +
                     "ct.coach_name, t.train_number, t.train_name " +
                     "FROM train_coaches c " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "JOIN trains t ON c.train_id = t.train_id " +
                     "WHERE c.coach_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, coachId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCoach(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CoachDAO] getCoachById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<TrainCoach> getCoachesByTrainId(int trainId) {
        List<TrainCoach> list = new ArrayList<>();
        String sql = "SELECT c.coach_id, c.train_id, c.coach_type_id, c.coach_number, c.total_seats, c.is_active, c.created_at, c.updated_at, " +
                     "ct.coach_name, t.train_number, t.train_name " +
                     "FROM train_coaches c " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "JOIN trains t ON c.train_id = t.train_id " +
                     "WHERE c.train_id = ? ORDER BY c.coach_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCoach(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[CoachDAO] getCoachesByTrainId error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<TrainCoach> getCoachesByTrainAndType(int trainId, int coachTypeId) {
        List<TrainCoach> list = new ArrayList<>();
        String sql = "SELECT c.coach_id, c.train_id, c.coach_type_id, c.coach_number, c.total_seats, c.is_active, c.created_at, c.updated_at, " +
                     "ct.coach_name, t.train_number, t.train_name " +
                     "FROM train_coaches c " +
                     "JOIN coach_type ct ON c.coach_type_id = ct.coach_type_id " +
                     "JOIN trains t ON c.train_id = t.train_id " +
                     "WHERE c.train_id = ? AND c.coach_type_id = ? AND c.is_active = true ORDER BY c.coach_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, coachTypeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCoach(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[CoachDAO] getCoachesByTrainAndType error: " + e.getMessage());
        }
        return list;
    }

    private TrainCoach mapResultSetToCoach(ResultSet rs) throws SQLException {
        TrainCoach c = new TrainCoach();
        c.setCoachId(rs.getInt("coach_id"));
        c.setTrainId(rs.getInt("train_id"));
        c.setCoachTypeId(rs.getInt("coach_type_id"));
        c.setCoachNumber(rs.getString("coach_number"));
        c.setTotalSeats(rs.getInt("total_seats"));
        c.setActive(rs.getBoolean("is_active"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at"));
        c.setCoachTypeName(rs.getString("coach_name"));
        c.setTrainNumber(rs.getString("train_number"));
        c.setTrainName(rs.getString("train_name"));
        return c;
    }
}
