package com.railway.dao;

import java.sql.Connection;
import java.util.List;
import com.railway.model.Payment;
import com.railway.model.Refund;

public interface PaymentDAO {
    int recordPayment(Connection conn, Payment payment);
    Payment getPaymentByBookingId(int bookingId);
    Payment getPaymentById(int paymentId);
    int recordRefund(Connection conn, Refund refund);
    Refund getRefundByPaymentId(int paymentId);
    List<Payment> getAllPayments();
    List<Refund> getAllRefunds();
}
