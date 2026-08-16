package com.railway.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TrainFare {
    private int fareId;
    private int trainId;
    private int coachTypeId;
    private int sourceStationId;
    private int destinationStationId;
    private BigDecimal fare;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String coachTypeName;
    private String sourceStationName;
    private String destinationStationName;
    private String trainNumber;

    public TrainFare() {
    }

    public TrainFare(int fareId, int trainId, int coachTypeId, int sourceStationId, int destinationStationId, BigDecimal fare, Timestamp createdAt, Timestamp updatedAt) {
        this.fareId = fareId;
        this.trainId = trainId;
        this.coachTypeId = coachTypeId;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.fare = fare;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getFareId() {
        return fareId;
    }

    public void setFareId(int fareId) {
        this.fareId = fareId;
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

    public int getSourceStationId() {
        return sourceStationId;
    }

    public void setSourceStationId(int sourceStationId) {
        this.sourceStationId = sourceStationId;
    }

    public int getDestinationStationId() {
        return destinationStationId;
    }

    public void setDestinationStationId(int destinationStationId) {
        this.destinationStationId = destinationStationId;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
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

    public String getSourceStationName() {
        return sourceStationName;
    }

    public void setSourceStationName(String sourceStationName) {
        this.sourceStationName = sourceStationName;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public void setDestinationStationName(String destinationStationName) {
        this.destinationStationName = destinationStationName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    @Override
    public String toString() {
        return "TrainFare{" +
                "fareId=" + fareId +
                ", trainId=" + trainId +
                ", coachTypeId=" + coachTypeId +
                ", sourceStationId=" + sourceStationId +
                ", destinationStationId=" + destinationStationId +
                ", fare=" + fare +
                '}';
    }
}