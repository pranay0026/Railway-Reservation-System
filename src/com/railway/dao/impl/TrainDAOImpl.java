package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.TrainDAO;
import com.railway.model.Train;
import com.railway.util.DBConnection;

public class TrainDAOImpl implements TrainDAO {

    @Override
    public boolean addTrain(Train train) {
        String sql = "INSERT INTO trains (train_number, train_name, train_type_id, total_distance, is_active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, train.getTrainNumber().trim());
            pstmt.setString(2, train.getTrainName().trim());
            pstmt.setInt(3, train.getTrainTypeId());
            if (train.getTotalDistance() != null) {
                pstmt.setInt(4, train.getTotalDistance());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            pstmt.setBoolean(5, train.isActive());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TrainDAO] addTrain error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateTrain(Train train) {
        String sql = "UPDATE trains SET train_name = ?, train_type_id = ?, total_distance = ?, is_active = ? WHERE train_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, train.getTrainName().trim());
            pstmt.setInt(2, train.getTrainTypeId());
            if (train.getTotalDistance() != null) {
                pstmt.setInt(3, train.getTotalDistance());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.setBoolean(4, train.isActive());
            pstmt.setInt(5, train.getTrainId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TrainDAO] updateTrain error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean setTrainActive(int trainId, boolean active) {
        String sql = "UPDATE trains SET is_active = ? WHERE train_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, active);
            pstmt.setInt(2, trainId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TrainDAO] setTrainActive error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Train getTrainById(int trainId) {
        String sql = "SELECT t.train_id, t.train_number, t.train_name, t.train_type_id, t.total_distance, t.is_active, t.created_at, t.updated_at, tt.type_name " +
                     "FROM trains t LEFT JOIN train_types tt ON t.train_type_id = tt.train_type_id WHERE t.train_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTrain(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainDAO] getTrainById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Train getTrainByNumber(String trainNumber) {
        if (trainNumber == null) return null;
        String sql = "SELECT t.train_id, t.train_number, t.train_name, t.train_type_id, t.total_distance, t.is_active, t.created_at, t.updated_at, tt.type_name " +
                     "FROM trains t LEFT JOIN train_types tt ON t.train_type_id = tt.train_type_id WHERE UPPER(t.train_number) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trainNumber.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTrain(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainDAO] getTrainByNumber error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Train> searchTrains(String query) {
        List<Train> list = new ArrayList<>();
        String sql = "SELECT t.train_id, t.train_number, t.train_name, t.train_type_id, t.total_distance, t.is_active, t.created_at, t.updated_at, tt.type_name " +
                     "FROM trains t LEFT JOIN train_types tt ON t.train_type_id = tt.train_type_id " +
                     "WHERE UPPER(t.train_number) LIKE ? OR UPPER(t.train_name) LIKE ? ORDER BY t.train_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String wildcard = "%" + query.toUpperCase().trim() + "%";
            pstmt.setString(1, wildcard);
            pstmt.setString(2, wildcard);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTrain(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainDAO] searchTrains error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Train> getAllTrains() {
        List<Train> list = new ArrayList<>();
        String sql = "SELECT t.train_id, t.train_number, t.train_name, t.train_type_id, t.total_distance, t.is_active, t.created_at, t.updated_at, tt.type_name " +
                     "FROM trains t LEFT JOIN train_types tt ON t.train_type_id = tt.train_type_id ORDER BY t.train_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToTrain(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TrainDAO] getAllTrains error: " + e.getMessage());
        }
        return list;
    }

    private Train mapResultSetToTrain(ResultSet rs) throws SQLException {
        Train t = new Train();
        t.setTrainId(rs.getInt("train_id"));
        t.setTrainNumber(rs.getString("train_number"));
        t.setTrainName(rs.getString("train_name"));
        t.setTrainTypeId(rs.getInt("train_type_id"));
        int dist = rs.getInt("total_distance");
        t.setTotalDistance(rs.wasNull() ? null : dist);
        t.setActive(rs.getBoolean("is_active"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setUpdatedAt(rs.getTimestamp("updated_at"));
        t.setTrainTypeName(rs.getString("type_name"));
        return t;
    }
}
