package com.railway.model;

import java.sql.Timestamp;

public class AuditLog {
    private int logId;
    private int adminId;
    private String action;
    private String tableName;
    private int recordId;
    private Timestamp actionTime;

    // Helper display field
    private String adminUsername;

    public AuditLog() {
    }

    public AuditLog(int logId, int adminId, String action, String tableName, int recordId, Timestamp actionTime) {
        this.logId = logId;
        this.adminId = adminId;
        this.action = action;
        this.tableName = tableName;
        this.recordId = recordId;
        this.actionTime = actionTime;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Timestamp getActionTime() {
        return actionTime;
    }

    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "logId=" + logId +
                ", adminId=" + adminId +
                ", action='" + action + '\'' +
                ", tableName='" + tableName + '\'' +
                ", recordId=" + recordId +
                ", actionTime=" + actionTime +
                '}';
    }
}
