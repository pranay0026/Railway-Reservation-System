package com.railway.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Refund {
    private int refundId;
    private int paymentId;
    private BigDecimal refundAmount;
    private String refundReason;
    private String refundStatus = "PENDING";
    private Timestamp refundDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String transactionId;
    private String pnrNumber;

    public Refund() {
    }

    public Refund(int refundId, int paymentId, BigDecimal refundAmount, String refundReason, String refundStatus, Timestamp refundDate, Timestamp createdAt, Timestamp updatedAt) {
        this.refundId = refundId;
        this.paymentId = paymentId;
        this.refundAmount = refundAmount;
        this.refundReason = refundReason;
        this.refundStatus = refundStatus;
        this.refundDate = refundDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Timestamp getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Timestamp refundDate) {
        this.refundDate = refundDate;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "refundId=" + refundId +
                ", paymentId=" + paymentId +
                ", refundAmount=" + refundAmount +
                ", refundReason='" + refundReason + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", refundDate=" + refundDate +
                '}';
    }
}