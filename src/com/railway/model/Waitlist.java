package com.railway.model;

import java.sql.Timestamp;

public class Waitlist {
    private int waitlistId;
    private int passengerId;
    private int bookingId;
    private int waitlistNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String passengerName;
    private String pnrNumber;

    public Waitlist() {
    }

    public Waitlist(int waitlistId, int passengerId, int bookingId, int waitlistNumber, Timestamp createdAt, Timestamp updatedAt) {
        this.waitlistId = waitlistId;
        this.passengerId = passengerId;
        this.bookingId = bookingId;
        this.waitlistNumber = waitlistNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getWaitlistId() {
        return waitlistId;
    }

    public void setWaitlistId(int waitlistId) {
        this.waitlistId = waitlistId;
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

    public int getWaitlistNumber() {
        return waitlistNumber;
    }

    public void setWaitlistNumber(int waitlistNumber) {
        this.waitlistNumber = waitlistNumber;
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
        return "Waitlist{" +
                "waitlistId=" + waitlistId +
                ", passengerId=" + passengerId +
                ", bookingId=" + bookingId +
                ", waitlistNumber=" + waitlistNumber +
                '}';
    }
}