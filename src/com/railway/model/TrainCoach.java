package com.railway.model;

import java.sql.Timestamp;

public class TrainCoach {
    private int coachId;
    private int trainId;
    private int coachTypeId;
    private String coachNumber;
    private int totalSeats;
    private boolean active = true;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String coachTypeName;
    private String trainNumber;
    private String trainName;

    public TrainCoach() {
    }

    public TrainCoach(int coachId, int trainId, int coachTypeId, String coachNumber, int totalSeats, boolean active, Timestamp createdAt, Timestamp updatedAt) {
        this.coachId = coachId;
        this.trainId = trainId;
        this.coachTypeId = coachTypeId;
        this.coachNumber = coachNumber;
        this.totalSeats = totalSeats;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public int getCoachTypeId() {
        return coachTypeId;
    }

    public void setCoachTypeId(int coachTypeId) {
        this.coachTypeId = coachTypeId;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
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

    public String getCoachTypeName() {
        return coachTypeName;
    }

    public void setCoachTypeName(String coachTypeName) {
        this.coachTypeName = coachTypeName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    @Override
    public String toString() {
        return "TrainCoach{" +
                "coachId=" + coachId +
                ", trainId=" + trainId +
                ", coachTypeId=" + coachTypeId +
                ", coachTypeName='" + coachTypeName + '\'' +
                ", coachNumber='" + coachNumber + '\'' +
                ", totalSeats=" + totalSeats +
                ", active=" + active +
                '}';
    }
}
