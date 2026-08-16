package com.railway.model;

import java.sql.Date;
import java.sql.Timestamp;

public class TrainAvailability {
    private int availabilityId;
    private int trainId;
    private int coachId;
    private Date journeyDate;
    private int availableSeats = 0;
    private int racCount = 0;
    private int waitingCount = 0;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String coachNumber;
    private String coachTypeName;
    private int coachTypeId;
    private String trainNumber;
    private String trainName;

    public TrainAvailability() {
    }

    public TrainAvailability(int availabilityId, int trainId, int coachId, Date journeyDate, int availableSeats, int racCount, int waitingCount, Timestamp createdAt, Timestamp updatedAt) {
        this.availabilityId = availabilityId;
        this.trainId = trainId;
        this.coachId = coachId;
        this.journeyDate = journeyDate;
        this.availableSeats = availableSeats;
        this.racCount = racCount;
        this.waitingCount = waitingCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(int availabilityId) {
        this.availabilityId = availabilityId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getRacCount() {
        return racCount;
    }

    public void setRacCount(int racCount) {
        this.racCount = racCount;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
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

    public int getCoachTypeId() {
        return coachTypeId;
    }

    public void setCoachTypeId(int coachTypeId) {
        this.coachTypeId = coachTypeId;
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
        return "TrainAvailability{" +
                "availabilityId=" + availabilityId +
                ", trainId=" + trainId +
                ", coachId=" + coachId +
                ", coachNumber='" + coachNumber + '\'' +
                ", coachTypeName='" + coachTypeName + '\'' +
                ", journeyDate=" + journeyDate +
                ", availableSeats=" + availableSeats +
                ", racCount=" + racCount +
                ", waitingCount=" + waitingCount +
                '}';
    }
}