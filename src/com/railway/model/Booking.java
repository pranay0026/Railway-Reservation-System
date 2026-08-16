package com.railway.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private String pnrNumber;
    private int userId;
    private int trainId;
    private int sourceStationId;
    private int destinationStationId;
    private Date journeyDate;
    private Timestamp bookingDate;
    private int totalPassengers;
    private BigDecimal totalFare;
    private int bookingStatusId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String trainNumber;
    private String trainName;
    private String sourceStationName;
    private String destinationStationName;
    private String sourceStationCode;
    private String destinationStationCode;
    private String statusName;
    private String userFullName;

    public Booking() {
    }

    public Booking(int bookingId, String pnrNumber, int userId, int trainId, int sourceStationId, int destinationStationId, Date journeyDate, Timestamp bookingDate, int totalPassengers, BigDecimal totalFare, int bookingStatusId, Timestamp createdAt, Timestamp updatedAt) {
        this.bookingId = bookingId;
        this.pnrNumber = pnrNumber;
        this.userId = userId;
        this.trainId = trainId;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.journeyDate = journeyDate;
        this.bookingDate = bookingDate;
        this.totalPassengers = totalPassengers;
        this.totalFare = totalFare;
        this.bookingStatusId = bookingStatusId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
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

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(int totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public int getBookingStatusId() {
        return bookingStatusId;
    }

    public void setBookingStatusId(int bookingStatusId) {
        this.bookingStatusId = bookingStatusId;
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

    public String getSourceStationCode() {
        return sourceStationCode;
    }

    public void setSourceStationCode(String sourceStationCode) {
        this.sourceStationCode = sourceStationCode;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public void setDestinationStationCode(String destinationStationCode) {
        this.destinationStationCode = destinationStationCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", pnrNumber='" + pnrNumber + '\'' +
                ", trainNumber='" + trainNumber + '\'' +
                ", trainName='" + trainName + '\'' +
                ", journeyDate=" + journeyDate +
                ", totalPassengers=" + totalPassengers +
                ", totalFare=" + totalFare +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}