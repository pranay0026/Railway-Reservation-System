package com.railway.service.impl;

import java.util.List;
import com.railway.dao.PaymentDAO;
import com.railway.dao.impl.PaymentDAOImpl;
import com.railway.model.Payment;
import com.railway.model.Refund;
import com.railway.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentDAO paymentDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    @Override
    public List<Refund> getAllRefunds() {
        return paymentDAO.getAllRefunds();
    }

    @Override
    public Payment getPaymentByBookingId(int bookingId) {
        return paymentDAO.getPaymentByBookingId(bookingId);
    }
}
