package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.AdminDAO;
import com.railway.model.Admin;
import com.railway.model.AuditLog;
import com.railway.util.DBConnection;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin loginAdmin(String username, String password) {
        String sql = "SELECT admin_id, username, password, full_name, role, created_at FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] loginAdmin error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Admin getAdminById(int adminId) {
        String sql = "SELECT admin_id, username, password, full_name, role, created_at FROM admins WHERE admin_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] getAdminById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT admin_id, username, password, full_name, role, created_at FROM admins WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] getAdminByUsername error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean addAdmin(Admin admin) {
        String sql = "INSERT INTO admins (username, password, full_name, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPassword());
            pstmt.setString(3, admin.getFullName());
            pstmt.setString(4, admin.getRole() != null ? admin.getRole() : "Admin");

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[AdminDAO] addAdmin error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        String sql = "UPDATE admins SET full_name = ?, role = ? WHERE admin_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, admin.getFullName());
            pstmt.setString(2, admin.getRole());
            pstmt.setInt(3, admin.getAdminId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[AdminDAO] updateAdmin error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT admin_id, username, password, full_name, role, created_at FROM admins ORDER BY admin_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] getAllAdmins error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void logAudit(Connection conn, int adminId, String action, String tableName, int recordId) {
        String sql = "INSERT INTO audit_logs (admin_id, action, table_name, record_id) VALUES (?, ?, ?, ?)";
        boolean closeConn = false;
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, adminId);
                pstmt.setString(2, action);
                pstmt.setString(3, tableName);
                pstmt.setInt(4, recordId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] logAudit error: " + e.getMessage());
        }
    }

    @Override
    public List<AuditLog> getAuditLogs(int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT l.log_id, l.admin_id, l.action, l.table_name, l.record_id, l.action_time, a.username " +
                     "FROM audit_logs l JOIN admins a ON l.admin_id = a.admin_id ORDER BY l.action_time DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit > 0 ? limit : 50);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog(
                            rs.getInt("log_id"),
                            rs.getInt("admin_id"),
                            rs.getString("action"),
                            rs.getString("table_name"),
                            rs.getInt("record_id"),
                            rs.getTimestamp("action_time")
                    );
                    log.setAdminUsername(rs.getString("username"));
                    list.add(log);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] getAuditLogs error: " + e.getMessage());
        }
        return list;
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getInt("admin_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("full_name"),
                rs.getString("role"),
                rs.getTimestamp("created_at")
        );
    }
}
