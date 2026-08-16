package com.railway.service;

import java.math.BigDecimal;
import java.util.List;
import com.railway.exception.CancellationException;
import com.railway.exception.RailwayException;
import com.railway.model.Cancellation;
import com.railway.model.Refund;

public interface CancellationService {
    Cancellation cancelBooking(int bookingId, int userId, String reason) throws CancellationException, RailwayException;
    BigDecimal calculateRefundAmount(BigDecimal totalFare, String bookingStatus);
    BigDecimal calculateCancellationCharge(BigDecimal totalFare, String bookingStatus);
    Cancellation getCancellationDetails(int bookingId);
    Refund getRefundDetails(int paymentId);
    List<Cancellation> getAllCancellations();
}
