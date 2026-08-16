package com.railway.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int bookingId;
    private int paymentMethodId;
    private int paymentStatusId;
    private String transactionId;
    private BigDecimal amount;
    private Timestamp paymentDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Helper display fields
    private String paymentMethodName;
    private String paymentStatusName;

    public Payment() {
    }

    public Payment(int paymentId, int bookingId, int paymentMethodId, int paymentStatusId, String transactionId, BigDecimal amount, Timestamp paymentDate, Timestamp createdAt, Timestamp updatedAt) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentMethodId = paymentMethodId;
        this.paymentStatusId = paymentStatusId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public int getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
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

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentStatusName() {
        return paymentStatusName;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.paymentStatusName = paymentStatusName;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", bookingId=" + bookingId +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", paymentMethodName='" + paymentMethodName + '\'' +
                ", paymentStatusName='" + paymentStatusName + '\'' +
                '}';
    }
}