package com.railway.model;

import java.sql.Timestamp;

public class Ticket {
    private int ticketId;
    private int bookingId;
    private String ticketNumber;
    private Timestamp issueDate;
    private String qrCode;
    private String ticketPdf;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Ticket() {
    }

    public Ticket(int ticketId, int bookingId, String ticketNumber, Timestamp issueDate, String qrCode, String ticketPdf, Timestamp createdAt, Timestamp updatedAt) {
        this.ticketId = ticketId;
        this.bookingId = bookingId;
        this.ticketNumber = ticketNumber;
        this.issueDate = issueDate;
        this.qrCode = qrCode;
        this.ticketPdf = ticketPdf;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Timestamp getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTicketPdf() {
        return ticketPdf;
    }

    public void setTicketPdf(String ticketPdf) {
        this.ticketPdf = ticketPdf;
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

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", bookingId=" + bookingId +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", issueDate=" + issueDate +
                '}';
    }
}
