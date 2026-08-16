package com.railway.model;

import java.sql.Timestamp;

public class BookingPassenger {
    private int passengerId;
    private int bookingId;
    private String passengerName;
    private int age;
    private String gender;
    private String berthPreference = "NA";
    private Integer seatId;
    private int bookingStatusId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String coachNumber;
    private Integer seatNumber;
    private String berthType;
    private String statusName;
    private Integer racNumber;
    private Integer waitlistNumber;

    public BookingPassenger() {
    }

    public BookingPassenger(int passengerId, int bookingId, String passengerName, int age, String gender, String berthPreference, Integer seatId, int bookingStatusId, Timestamp createdAt, Timestamp updatedAt) {
        this.passengerId = passengerId;
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.berthPreference = berthPreference;
        this.seatId = seatId;
        this.bookingStatusId = bookingStatusId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBerthPreference() {
        return berthPreference;
    }

    public void setBerthPreference(String berthPreference) {
        this.berthPreference = berthPreference;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
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

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBerthType() {
        return berthType;
    }

    public void setBerthType(String berthType) {
        this.berthType = berthType;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getRacNumber() {
        return racNumber;
    }

    public void setRacNumber(Integer racNumber) {
        this.racNumber = racNumber;
    }

    public Integer getWaitlistNumber() {
        return waitlistNumber;
    }

    public void setWaitlistNumber(Integer waitlistNumber) {
        this.waitlistNumber = waitlistNumber;
    }

    @Override
    public String toString() {
        return "BookingPassenger{" +
                "passengerId=" + passengerId +
                ", passengerName='" + passengerName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", statusName='" + statusName + '\'' +
                ", coachNumber='" + coachNumber + '\'' +
                ", seatNumber=" + seatNumber +
                ", berthType='" + berthType + '\'' +
                '}';
    }
}