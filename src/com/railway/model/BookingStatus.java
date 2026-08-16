package com.railway.model;

public class BookingStatus {
    private int bookingStatusId;
    private String statusName;
    private String description;

    public BookingStatus() {
    }

    public BookingStatus(int bookingStatusId, String statusName, String description) {
        this.bookingStatusId = bookingStatusId;
        this.statusName = statusName;
        this.description = description;
    }

    public int getBookingStatusId() {
        return bookingStatusId;
    }

    public void setBookingStatusId(int bookingStatusId) {
        this.bookingStatusId = bookingStatusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BookingStatus{" +
                "bookingStatusId=" + bookingStatusId +
                ", statusName='" + statusName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
