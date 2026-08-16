package com.railway.service;

import java.util.List;
import com.railway.model.Payment;
import com.railway.model.Refund;

public interface PaymentService {
    List<Payment> getAllPayments();
    List<Refund> getAllRefunds();
    Payment getPaymentByBookingId(int bookingId);
}
