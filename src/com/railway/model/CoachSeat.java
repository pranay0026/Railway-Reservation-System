package com.railway.model;

import java.sql.Timestamp;

public class CoachSeat {
    private int seatId;
    private int coachId;
    private int seatNumber;
    private String berthType;
    private boolean active = true;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String coachNumber;
    private String coachTypeName;

    public CoachSeat() {
    }

    public CoachSeat(int seatId, int coachId, int seatNumber, String berthType, boolean active, Timestamp createdAt, Timestamp updatedAt) {
        this.seatId = seatId;
        this.coachId = coachId;
        this.seatNumber = seatNumber;
        this.berthType = berthType;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBerthType() {
        return berthType;
    }

    public void setBerthType(String berthType) {
        this.berthType = berthType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getCoachTypeName() {
        return coachTypeName;
    }

    public void setCoachTypeName(String coachTypeName) {
        this.coachTypeName = coachTypeName;
    }

    @Override
    public String toString() {
        return "CoachSeat{" +
                "seatId=" + seatId +
                ", coachId=" + coachId +
                ", coachNumber='" + coachNumber + '\'' +
                ", seatNumber=" + seatNumber +
                ", berthType='" + berthType + '\'' +
                ", active=" + active +
                '}';
    }
}