package com.railway.dao;

import java.sql.Connection;
import java.util.List;
import com.railway.model.Booking;
import com.railway.model.BookingPassenger;
import com.railway.model.Ticket;

public interface BookingDAO {
    int createBooking(Connection conn, Booking booking);
    int addBookingPassenger(Connection conn, BookingPassenger passenger);
    boolean updateBookingStatus(Connection conn, int bookingId, int statusId);
    boolean updatePassengerStatusAndSeat(Connection conn, int passengerId, int statusId, Integer seatId);
    Booking getBookingById(int bookingId);
    Booking getBookingByPNR(String pnr);
    List<Booking> getBookingsByUserId(int userId);
    List<BookingPassenger> getPassengersByBookingId(int bookingId);
    boolean createTicket(Connection conn, Ticket ticket);
    Ticket getTicketByBookingId(int bookingId);
    List<Booking> getAllBookings();
}
