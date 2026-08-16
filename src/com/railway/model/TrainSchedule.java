package com.railway.model;

import java.sql.Date;
import java.sql.Timestamp;

public class TrainSchedule {
    private int scheduleId;
    private int trainId;
    private Date journeyDate;
    private String runningStatus = "Running";
    private int delayMinutes = 0;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String trainNumber;
    private String trainName;

    public TrainSchedule() {
    }

    public TrainSchedule(int scheduleId, int trainId, Date journeyDate, String runningStatus, int delayMinutes, Timestamp createdAt, Timestamp updatedAt) {
        this.scheduleId = scheduleId;
        this.trainId = trainId;
        this.journeyDate = journeyDate;
        this.runningStatus = runningStatus;
        this.delayMinutes = delayMinutes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
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
        return "TrainSchedule{" +
                "scheduleId=" + scheduleId +
                ", trainId=" + trainId +
                ", journeyDate=" + journeyDate +
                ", runningStatus='" + runningStatus + '\'' +
                ", delayMinutes=" + delayMinutes +
                '}';
    }
}