package com.railway.dao;

import java.sql.Connection;
import java.util.List;
import com.railway.model.Admin;
import com.railway.model.AuditLog;

public interface AdminDAO {
    Admin loginAdmin(String username, String password);
    Admin getAdminById(int adminId);
    Admin getAdminByUsername(String username);
    boolean addAdmin(Admin admin);
    boolean updateAdmin(Admin admin);
    List<Admin> getAllAdmins();
    void logAudit(Connection conn, int adminId, String action, String tableName, int recordId);
    List<AuditLog> getAuditLogs(int limit);
}
