package com.railway.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Cancellation {
    private int cancellationId;
    private int bookingId;
    private int cancelledBy;
    private Timestamp cancellationDate;
    private String cancellationReason;
    private BigDecimal cancellationCharge = BigDecimal.ZERO;
    private BigDecimal refundAmount = BigDecimal.ZERO;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String pnrNumber;
    private String cancelledByUserName;

    public Cancellation() {
    }

    public Cancellation(int cancellationId, int bookingId, int cancelledBy, Timestamp cancellationDate, String cancellationReason, BigDecimal cancellationCharge, BigDecimal refundAmount, Timestamp createdAt, Timestamp updatedAt) {
        this.cancellationId = cancellationId;
        this.bookingId = bookingId;
        this.cancelledBy = cancelledBy;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
        this.cancellationCharge = cancellationCharge;
        this.refundAmount = refundAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getCancellationId() {
        return cancellationId;
    }

    public void setCancellationId(int cancellationId) {
        this.cancellationId = cancellationId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(int cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Timestamp getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Timestamp cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public BigDecimal getCancellationCharge() {
        return cancellationCharge;
    }

    public void setCancellationCharge(BigDecimal cancellationCharge) {
        this.cancellationCharge = cancellationCharge;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
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

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public String getCancelledByUserName() {
        return cancelledByUserName;
    }

    public void setCancelledByUserName(String cancelledByUserName) {
        this.cancelledByUserName = cancelledByUserName;
    }

    @Override
    public String toString() {
        return "Cancellation{" +
                "cancellationId=" + cancellationId +
                ", bookingId=" + bookingId +
                ", cancelledBy=" + cancelledBy +
                ", cancellationDate=" + cancellationDate +
                ", cancellationCharge=" + cancellationCharge +
                ", refundAmount=" + refundAmount +
                ", cancellationReason='" + cancellationReason + '\'' +
                '}';
    }
}