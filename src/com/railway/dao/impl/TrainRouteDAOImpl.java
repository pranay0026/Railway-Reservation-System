package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.TrainRouteDAO;
import com.railway.model.Train;
import com.railway.model.TrainRoute;
import com.railway.util.DBConnection;

public class TrainRouteDAOImpl implements TrainRouteDAO {

    @Override
    public boolean addRouteStop(TrainRoute route) {
        String sql = "INSERT INTO train_routes (train_id, station_id, stop_number, arrival_time, departure_time, distance_from_source, platform_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, route.getTrainId());
            pstmt.setInt(2, route.getStationId());
            pstmt.setInt(3, route.getStopNumber());
            pstmt.setTime(4, route.getArrivalTime());
            pstmt.setTime(5, route.getDepartureTime());
            pstmt.setInt(6, route.getDistanceFromSource());
            pstmt.setString(7, route.getPlatformNumber());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TrainRouteDAO] addRouteStop error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateRouteStop(TrainRoute route) {
        String sql = "UPDATE train_routes SET stop_number = ?, arrival_time = ?, departure_time = ?, distance_from_source = ?, platform_number = ? " +
                     "WHERE route_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, route.getStopNumber());
            pstmt.setTime(2, route.getArrivalTime());
            pstmt.setTime(3, route.getDepartureTime());
            pstmt.setInt(4, route.getDistanceFromSource());
            pstmt.setString(5, route.getPlatformNumber());
            pstmt.setInt(6, route.getRouteId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TrainRouteDAO] updateRouteStop error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteRouteStop(int routeId) {
        String sql = "DELETE FROM train_routes WHERE route_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, routeId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TrainRouteDAO] deleteRouteStop error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<TrainRoute> getRoutesByTrainId(int trainId) {
        List<TrainRoute> list = new ArrayList<>();
        String sql = "SELECT tr.route_id, tr.train_id, tr.station_id, tr.stop_number, tr.arrival_time, tr.departure_time, " +
                     "tr.distance_from_source, tr.platform_number, s.station_code, s.station_name, t.train_number, t.train_name " +
                     "FROM train_routes tr " +
                     "JOIN stations s ON tr.station_id = s.station_id " +
                     "JOIN trains t ON tr.train_id = t.train_id " +
                     "WHERE tr.train_id = ? ORDER BY tr.stop_number ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TrainRoute r = new TrainRoute(
                            rs.getInt("route_id"),
                            rs.getInt("train_id"),
                            rs.getInt("station_id"),
                            rs.getInt("stop_number"),
                            rs.getTime("arrival_time"),
                            rs.getTime("departure_time"),
                            rs.getInt("distance_from_source"),
                            rs.getString("platform_number")
                    );
                    r.setStationCode(rs.getString("station_code"));
                    r.setStationName(rs.getString("station_name"));
                    r.setTrainNumber(rs.getString("train_number"));
                    r.setTrainName(rs.getString("train_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainRouteDAO] getRoutesByTrainId error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public TrainRoute getRouteStop(int trainId, int stationId) {
        String sql = "SELECT tr.route_id, tr.train_id, tr.station_id, tr.stop_number, tr.arrival_time, tr.departure_time, " +
                     "tr.distance_from_source, tr.platform_number, s.station_code, s.station_name " +
                     "FROM train_routes tr " +
                     "JOIN stations s ON tr.station_id = s.station_id " +
                     "WHERE tr.train_id = ? AND tr.station_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, stationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TrainRoute r = new TrainRoute(
                            rs.getInt("route_id"),
                            rs.getInt("train_id"),
                            rs.getInt("station_id"),
                            rs.getInt("stop_number"),
                            rs.getTime("arrival_time"),
                            rs.getTime("departure_time"),
                            rs.getInt("distance_from_source"),
                            rs.getString("platform_number")
                    );
                    r.setStationCode(rs.getString("station_code"));
                    r.setStationName(rs.getString("station_name"));
                    return r;
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainRouteDAO] getRouteStop error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Train> findTrainsBetweenStations(int sourceStationId, int destinationStationId) {
        List<Train> list = new ArrayList<>();
        String sql = "SELECT t.train_id, t.train_number, t.train_name, t.train_type_id, t.total_distance, t.is_active, " +
                     "t.created_at, t.updated_at, tt.type_name " +
                     "FROM trains t " +
                     "JOIN train_routes r_src ON t.train_id = r_src.train_id " +
                     "JOIN train_routes r_dst ON t.train_id = r_dst.train_id " +
                     "LEFT JOIN train_types tt ON t.train_type_id = tt.train_type_id " +
                     "WHERE r_src.station_id = ? " +
                     "  AND r_dst.station_id = ? " +
                     "  AND r_src.stop_number < r_dst.stop_number " +
                     "  AND t.is_active = true " +
                     "ORDER BY t.train_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sourceStationId);
            pstmt.setInt(2, destinationStationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
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
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("[TrainRouteDAO] findTrainsBetweenStations error: " + e.getMessage());
        }
        return list;
    }
}
