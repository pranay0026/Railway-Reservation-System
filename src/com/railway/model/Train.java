package com.railway.model;

import java.sql.Timestamp;

public class Train {
    private int trainId;
    private String trainNumber;
    private String trainName;
    private int trainTypeId;
    private Integer totalDistance;
    private boolean active = true;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display field
    private String trainTypeName;

    public Train() {
    }

    public Train(int trainId, String trainNumber, String trainName, int trainTypeId, Integer totalDistance, boolean active, Timestamp createdAt, Timestamp updatedAt) {
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.trainTypeId = trainTypeId;
        this.totalDistance = totalDistance;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
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

    public int getTrainTypeId() {
        return trainTypeId;
    }

    public void setTrainTypeId(int trainTypeId) {
        this.trainTypeId = trainTypeId;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
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

    public String getTrainTypeName() {
        return trainTypeName;
    }

    public void setTrainTypeName(String trainTypeName) {
        this.trainTypeName = trainTypeName;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId=" + trainId +
                ", trainNumber='" + trainNumber + '\'' +
                ", trainName='" + trainName + '\'' +
                ", trainTypeId=" + trainTypeId +
                ", totalDistance=" + totalDistance +
                ", active=" + active +
                '}';
    }
}
