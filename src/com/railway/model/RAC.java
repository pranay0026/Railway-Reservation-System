package com.railway.model;

import java.sql.Timestamp;

public class RAC {
    private int racId;
    private int passengerId;
    private int bookingId;
    private int racNumber;
    private Integer seatId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String passengerName;
    private String pnrNumber;

    public RAC() {
    }

    public RAC(int racId, int passengerId, int bookingId, int racNumber, Integer seatId, Timestamp createdAt, Timestamp updatedAt) {
        this.racId = racId;
        this.passengerId = passengerId;
        this.bookingId = bookingId;
        this.racNumber = racNumber;
        this.seatId = seatId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getRacId() {
        return racId;
    }

    public void setRacId(int racId) {
        this.racId = racId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRacNumber() {
        return racNumber;
    }

    public void setRacNumber(int racNumber) {
        this.racNumber = racNumber;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
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

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    @Override
    public String toString() {
        return "RAC{" +
                "racId=" + racId +
                ", passengerId=" + passengerId +
                ", bookingId=" + bookingId +
                ", racNumber=" + racNumber +
                ", seatId=" + seatId +
                '}';
    }
}
