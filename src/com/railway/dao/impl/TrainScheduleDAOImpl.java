package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.TrainScheduleDAO;
import com.railway.model.TrainSchedule;
import com.railway.util.DBConnection;

public class TrainScheduleDAOImpl implements TrainScheduleDAO {

    @Override
    public boolean addOrUpdateSchedule(TrainSchedule schedule) {
        String sql = "INSERT INTO train_schedule (train_id, journey_date, running_status, delay_minutes) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE running_status = VALUES(running_status), delay_minutes = VALUES(delay_minutes)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getTrainId());
            pstmt.setDate(2, schedule.getJourneyDate());
            pstmt.setString(3, schedule.getRunningStatus());
            pstmt.setInt(4, schedule.getDelayMinutes());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TrainScheduleDAO] addOrUpdateSchedule error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public TrainSchedule getSchedule(int trainId, Date journeyDate) {
        String sql = "SELECT s.schedule_id, s.train_id, s.journey_date, s.running_status, s.delay_minutes, s.created_at, s.updated_at, " +
                     "t.train_number, t.train_name " +
                     "FROM train_schedule s JOIN trains t ON s.train_id = t.train_id " +
                     "WHERE s.train_id = ? AND s.journey_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setDate(2, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TrainSchedule ts = new TrainSchedule(
                            rs.getInt("schedule_id"),
                            rs.getInt("train_id"),
                            rs.getDate("journey_date"),
                            rs.getString("running_status"),
                            rs.getInt("delay_minutes"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    ts.setTrainNumber(rs.getString("train_number"));
                    ts.setTrainName(rs.getString("train_name"));
                    return ts;
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainScheduleDAO] getSchedule error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<TrainSchedule> getSchedulesByTrain(int trainId) {
        List<TrainSchedule> list = new ArrayList<>();
        String sql = "SELECT s.schedule_id, s.train_id, s.journey_date, s.running_status, s.delay_minutes, s.created_at, s.updated_at, " +
                     "t.train_number, t.train_name " +
                     "FROM train_schedule s JOIN trains t ON s.train_id = t.train_id " +
                     "WHERE s.train_id = ? ORDER BY s.journey_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TrainSchedule ts = new TrainSchedule(
                            rs.getInt("schedule_id"),
                            rs.getInt("train_id"),
                            rs.getDate("journey_date"),
                            rs.getString("running_status"),
                            rs.getInt("delay_minutes"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    ts.setTrainNumber(rs.getString("train_number"));
                    ts.setTrainName(rs.getString("train_name"));
                    list.add(ts);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainScheduleDAO] getSchedulesByTrain error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<TrainSchedule> getSchedulesByDate(Date journeyDate) {
        List<TrainSchedule> list = new ArrayList<>();
        String sql = "SELECT s.schedule_id, s.train_id, s.journey_date, s.running_status, s.delay_minutes, s.created_at, s.updated_at, " +
                     "t.train_number, t.train_name " +
                     "FROM train_schedule s JOIN trains t ON s.train_id = t.train_id " +
                     "WHERE s.journey_date = ? ORDER BY t.train_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, journeyDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TrainSchedule ts = new TrainSchedule(
                            rs.getInt("schedule_id"),
                            rs.getInt("train_id"),
                            rs.getDate("journey_date"),
                            rs.getString("running_status"),
                            rs.getInt("delay_minutes"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    ts.setTrainNumber(rs.getString("train_number"));
                    ts.setTrainName(rs.getString("train_name"));
                    list.add(ts);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainScheduleDAO] getSchedulesByDate error: " + e.getMessage());
        }
        return list;
    }
}
