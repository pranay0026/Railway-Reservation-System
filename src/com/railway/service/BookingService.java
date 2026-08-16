package com.railway.service;

import java.time.LocalDate;
import java.util.List;
import com.railway.exception.BookingException;
import com.railway.exception.PaymentException;
import com.railway.exception.RailwayException;
import com.railway.exception.SeatUnavailableException;
import com.railway.model.Booking;
import com.railway.model.BookingPassenger;
import com.railway.model.Payment;
import com.railway.model.Ticket;

public interface BookingService {
    Booking bookTicket(int userId, int trainId, int coachTypeId, int sourceStationId, int destinationStationId,
                       LocalDate journeyDate, List<BookingPassenger> passengers, int paymentMethodId)
            throws BookingException, SeatUnavailableException, PaymentException, RailwayException;

    Booking getBookingByPNR(String pnr) throws RailwayException;
    Booking getBookingById(int bookingId) throws RailwayException;
    List<Booking> getUserBookings(int userId);
    List<BookingPassenger> getBookingPassengers(int bookingId);
    Ticket getTicket(int bookingId);
    Payment getPayment(int bookingId);
    List<Booking> getAllBookings();
}
