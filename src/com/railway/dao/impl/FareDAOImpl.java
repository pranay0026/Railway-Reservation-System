package com.railway.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.FareDAO;
import com.railway.model.TrainFare;
import com.railway.util.DBConnection;

public class FareDAOImpl implements FareDAO {

    @Override
    public boolean addFare(TrainFare fare) {
        String sql = "INSERT INTO train_fare (train_id, coach_type_id, source_station_id, destination_station_id, fare) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE fare = VALUES(fare)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fare.getTrainId());
            pstmt.setInt(2, fare.getCoachTypeId());
            pstmt.setInt(3, fare.getSourceStationId());
            pstmt.setInt(4, fare.getDestinationStationId());
            pstmt.setBigDecimal(5, fare.getFare());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[FareDAO] addFare error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateFare(TrainFare fare) {
        String sql = "UPDATE train_fare SET fare = ? WHERE fare_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, fare.getFare());
            pstmt.setInt(2, fare.getFareId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[FareDAO] updateFare error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public TrainFare getFare(int trainId, int coachTypeId, int sourceStationId, int destinationStationId) {
        String sql = "SELECT f.fare_id, f.train_id, f.coach_type_id, f.source_station_id, f.destination_station_id, f.fare, " +
                     "f.created_at, f.updated_at, ct.coach_name, s1.station_name AS src_name, s2.station_name AS dst_name, t.train_number " +
                     "FROM train_fare f " +
                     "JOIN coach_type ct ON f.coach_type_id = ct.coach_type_id " +
                     "JOIN stations s1 ON f.source_station_id = s1.station_id " +
                     "JOIN stations s2 ON f.destination_station_id = s2.station_id " +
                     "JOIN trains t ON f.train_id = t.train_id " +
                     "WHERE f.train_id = ? AND f.coach_type_id = ? AND f.source_station_id = ? AND f.destination_station_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, coachTypeId);
            pstmt.setInt(3, sourceStationId);
            pstmt.setInt(4, destinationStationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFare(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[FareDAO] getFare error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<TrainFare> getFaresByTrain(int trainId) {
        List<TrainFare> list = new ArrayList<>();
        String sql = "SELECT f.fare_id, f.train_id, f.coach_type_id, f.source_station_id, f.destination_station_id, f.fare, " +
                     "f.created_at, f.updated_at, ct.coach_name, s1.station_name AS src_name, s2.station_name AS dst_name, t.train_number " +
                     "FROM train_fare f " +
                     "JOIN coach_type ct ON f.coach_type_id = ct.coach_type_id " +
                     "JOIN stations s1 ON f.source_station_id = s1.station_id " +
                     "JOIN stations s2 ON f.destination_station_id = s2.station_id " +
                     "JOIN trains t ON f.train_id = t.train_id " +
                     "WHERE f.train_id = ? ORDER BY f.coach_type_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToFare(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[FareDAO] getFaresByTrain error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public BigDecimal calculateFare(int trainId, int coachTypeId, int sourceStationId, int destinationStationId) {
        // First try exact match in train_fare table
        TrainFare tf = getFare(trainId, coachTypeId, sourceStationId, destinationStationId);
        if (tf != null && tf.getFare() != null) {
            return tf.getFare();
        }

        // If not exact match, calculate based on route distance
        String sql = "SELECT ABS(r2.distance_from_source - r1.distance_from_source) AS dist " +
                     "FROM train_routes r1, train_routes r2 " +
                     "WHERE r1.train_id = ? AND r1.station_id = ? AND r2.train_id = ? AND r2.station_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, sourceStationId);
            pstmt.setInt(3, trainId);
            pstmt.setInt(4, destinationStationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int dist = rs.getInt("dist");
                    if (dist <= 0) dist = 100;
                    // Class rate multiplier
                    double ratePerKm = 0.50; // default for 2S/SL
                    if (coachTypeId == 1) ratePerKm = 0.60; // SL
                    else if (coachTypeId == 2) ratePerKm = 1.20; // 3A
                    else if (coachTypeId == 3) ratePerKm = 1.80; // 2A
                    else if (coachTypeId == 4) ratePerKm = 2.50; // 1A
                    else if (coachTypeId == 5) ratePerKm = 1.00; // CC
                    else if (coachTypeId == 6) ratePerKm = 0.40; // 2S

                    return BigDecimal.valueOf(Math.round(dist * ratePerKm * 100.0) / 100.0);
                }
            }
        } catch (SQLException e) {
            System.err.println("[FareDAO] calculateFare distance error: " + e.getMessage());
        }

        // Default base fallback
        return BigDecimal.valueOf(500.00);
    }

    private TrainFare mapResultSetToFare(ResultSet rs) throws SQLException {
        TrainFare f = new TrainFare();
        f.setFareId(rs.getInt("fare_id"));
        f.setTrainId(rs.getInt("train_id"));
        f.setCoachTypeId(rs.getInt("coach_type_id"));
        f.setSourceStationId(rs.getInt("source_station_id"));
        f.setDestinationStationId(rs.getInt("destination_station_id"));
        f.setFare(rs.getBigDecimal("fare"));
        f.setCreatedAt(rs.getTimestamp("created_at"));
        f.setUpdatedAt(rs.getTimestamp("updated_at"));
        f.setCoachTypeName(rs.getString("coach_name"));
        f.setSourceStationName(rs.getString("src_name"));
        f.setDestinationStationName(rs.getString("dst_name"));
        f.setTrainNumber(rs.getString("train_number"));
        return f;
    }
}
