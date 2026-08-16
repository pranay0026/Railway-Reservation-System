package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.StationDAO;
import com.railway.model.Station;
import com.railway.util.DBConnection;

public class StationDAOImpl implements StationDAO {

    @Override
    public boolean addStation(Station station) {
        String sql = "INSERT INTO stations (station_code, station_name, city, state, zone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, station.getStationCode().toUpperCase().trim());
            pstmt.setString(2, station.getStationName().trim());
            pstmt.setString(3, station.getCity().trim());
            pstmt.setString(4, station.getState().trim());
            pstmt.setString(5, station.getZone());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[StationDAO] addStation error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateStation(Station station) {
        String sql = "UPDATE stations SET station_name = ?, city = ?, state = ?, zone = ? WHERE station_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, station.getStationName());
            pstmt.setString(2, station.getCity());
            pstmt.setString(3, station.getState());
            pstmt.setString(4, station.getZone());
            pstmt.setInt(5, station.getStationId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[StationDAO] updateStation error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Station getStationById(int stationId) {
        String sql = "SELECT station_id, station_code, station_name, city, state, zone FROM stations WHERE station_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStation(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[StationDAO] getStationById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Station getStationByCode(String code) {
        if (code == null) return null;
        String sql = "SELECT station_id, station_code, station_name, city, state, zone FROM stations WHERE UPPER(station_code) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStation(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[StationDAO] getStationByCode error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Station> searchStations(String query) {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT station_id, station_code, station_name, city, state, zone FROM stations " +
                     "WHERE UPPER(station_code) LIKE ? OR UPPER(station_name) LIKE ? OR UPPER(city) LIKE ? ORDER BY station_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String wildcard = "%" + query.toUpperCase().trim() + "%";
            pstmt.setString(1, wildcard);
            pstmt.setString(2, wildcard);
            pstmt.setString(3, wildcard);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToStation(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[StationDAO] searchStations error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Station> getAllStations() {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT station_id, station_code, station_name, city, state, zone FROM stations ORDER BY station_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToStation(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StationDAO] getAllStations error: " + e.getMessage());
        }
        return list;
    }

    private Station mapResultSetToStation(ResultSet rs) throws SQLException {
        Station s = new Station();
        s.setStationId(rs.getInt("station_id"));
        s.setStationCode(rs.getString("station_code"));
        s.setStationName(rs.getString("station_name"));
        s.setCity(rs.getString("city"));
        s.setState(rs.getString("state"));
        s.setZone(rs.getString("zone"));
        return s;
    }
}
