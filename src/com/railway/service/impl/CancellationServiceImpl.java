package com.railway.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import com.railway.dao.AvailabilityDAO;
import com.railway.dao.BookingDAO;
import com.railway.dao.CancellationDAO;
import com.railway.dao.CoachDAO;
import com.railway.dao.LookupDAO;
import com.railway.dao.PaymentDAO;
import com.railway.dao.SeatDAO;
import com.railway.dao.impl.AvailabilityDAOImpl;
import com.railway.dao.impl.BookingDAOImpl;
import com.railway.dao.impl.CancellationDAOImpl;
import com.railway.dao.impl.CoachDAOImpl;
import com.railway.dao.impl.LookupDAOImpl;
import com.railway.dao.impl.PaymentDAOImpl;
import com.railway.dao.impl.SeatDAOImpl;
import com.railway.exception.CancellationException;
import com.railway.exception.RailwayException;
import com.railway.model.Booking;
import com.railway.model.BookingPassenger;
import com.railway.model.Cancellation;
import com.railway.model.Payment;
import com.railway.model.RAC;
import com.railway.model.Refund;
import com.railway.model.TrainCoach;
import com.railway.model.Waitlist;
import com.railway.service.CancellationService;
import com.railway.util.DBConnection;

public class CancellationServiceImpl implements CancellationService {
    // Configurable refund percentages
    private static final BigDecimal CONFIRMED_REFUND_PERCENT = BigDecimal.valueOf(0.80); // 80% refund
    private static final BigDecimal RAC_REFUND_PERCENT = BigDecimal.valueOf(0.90);       // 90% refund
    private static final BigDecimal WAITLIST_REFUND_PERCENT = BigDecimal.valueOf(1.00);  // 100% refund

    private final BookingDAO bookingDAO;
    private final CancellationDAO cancellationDAO;
    private final PaymentDAO paymentDAO;
    private final LookupDAO lookupDAO;
    private final AvailabilityDAO availabilityDAO;
    private final SeatDAO seatDAO;
    private final CoachDAO coachDAO;

    public CancellationServiceImpl() {
        this.bookingDAO = new BookingDAOImpl();
        this.cancellationDAO = new CancellationDAOImpl();
        this.paymentDAO = new PaymentDAOImpl();
        this.lookupDAO = new LookupDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.seatDAO = new SeatDAOImpl();
        this.coachDAO = new CoachDAOImpl();
    }

    @Override
    public BigDecimal calculateRefundAmount(BigDecimal totalFare, String bookingStatus) {
        if (totalFare == null || totalFare.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        if ("CONFIRMED".equalsIgnoreCase(bookingStatus)) {
            return totalFare.multiply(CONFIRMED_REFUND_PERCENT).setScale(2, RoundingMode.HALF_UP);
        } else if ("RAC".equalsIgnoreCase(bookingStatus)) {
            return totalFare.multiply(RAC_REFUND_PERCENT).setScale(2, RoundingMode.HALF_UP);
        } else {
            return totalFare.multiply(WAITLIST_REFUND_PERCENT).setScale(2, RoundingMode.HALF_UP);
        }
    }

    @Override
    public BigDecimal calculateCancellationCharge(BigDecimal totalFare, String bookingStatus) {
        if (totalFare == null || totalFare.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal refund = calculateRefundAmount(totalFare, bookingStatus);
        return totalFare.subtract(refund).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Cancellation cancelBooking(int bookingId, int userId, String reason) throws CancellationException, RailwayException {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            throw new CancellationException("Booking not found with ID: " + bookingId);
        }
        if (booking.getUserId() != userId) {
            throw new CancellationException("Access denied: You can only cancel your own bookings.");
        }

        String currentStatus = booking.getStatusName();
        if ("CANCELLED".equalsIgnoreCase(currentStatus)) {
            throw new CancellationException("This booking is already cancelled.");
        }

        int cancelledStatusId = lookupDAO.getBookingStatusId("CANCELLED");
        int confirmedStatusId = lookupDAO.getBookingStatusId("CONFIRMED");
        int racStatusId = lookupDAO.getBookingStatusId("RAC");

        BigDecimal refundAmount = calculateRefundAmount(booking.getTotalFare(), currentStatus);
        BigDecimal cancelCharge = calculateCancellationCharge(booking.getTotalFare(), currentStatus);

        Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
        List<BookingPassenger> passengers = bookingDAO.getPassengersByBookingId(bookingId);

        Connection conn = null;
        try {
            conn = DBConnection.getNewConnection();
            conn.setAutoCommit(false);

            // 1. Update Booking status to CANCELLED
            bookingDAO.updateBookingStatus(conn, bookingId, cancelledStatusId);

            // 2. Process each passenger and RAC/Waitlist promotion
            int releasedSeatsCount = 0;
            Date journeyDate = booking.getJourneyDate();
            int trainId = booking.getTrainId();

            for (BookingPassenger p : passengers) {
                // Update passenger status to CANCELLED
                bookingDAO.updatePassengerStatusAndSeat(conn, p.getPassengerId(), cancelledStatusId, null);

                // If passenger had a confirmed seat allocated
                if (p.getSeatId() != null && p.getSeatId() > 0) {
                    Integer freedSeatId = p.getSeatId();

                    // Check if there is an eligible RAC passenger in queue
                    RAC nextRac = cancellationDAO.getNextRACPassenger(conn, trainId, journeyDate);
                    if (nextRac != null) {
                        // Promote RAC passenger to CONFIRMED and assign the freed seat
                        bookingDAO.updatePassengerStatusAndSeat(conn, nextRac.getPassengerId(), confirmedStatusId, freedSeatId);
                        cancellationDAO.removeRAC(conn, nextRac.getRacId());

                        // Now promote earliest Waitlist passenger to RAC
                        Waitlist nextWl = cancellationDAO.getNextWaitlistPassenger(conn, trainId, journeyDate);
                        if (nextWl != null) {
                            int newRacNumber = cancellationDAO.getNextRACNumber(conn, trainId, journeyDate);
                            bookingDAO.updatePassengerStatusAndSeat(conn, nextWl.getPassengerId(), racStatusId, null);
                            cancellationDAO.removeWaitlist(conn, nextWl.getWaitlistId());

                            RAC newRac = new RAC();
                            newRac.setPassengerId(nextWl.getPassengerId());
                            newRac.setBookingId(nextWl.getBookingId());
                            newRac.setRacNumber(newRacNumber);
                            newRac.setSeatId(null);
                            cancellationDAO.addToRAC(conn, newRac);
                        }
                    } else {
                        // No RAC passenger to promote; seat is officially freed
                        releasedSeatsCount++;
                    }
                } else if ("RAC".equalsIgnoreCase(p.getStatusName()) || p.getBookingStatusId() == racStatusId) {
                    // RAC passenger cancelled -> promote next waitlist to RAC
                    Waitlist nextWl = cancellationDAO.getNextWaitlistPassenger(conn, trainId, journeyDate);
                    if (nextWl != null) {
                        int newRacNumber = cancellationDAO.getNextRACNumber(conn, trainId, journeyDate);
                        bookingDAO.updatePassengerStatusAndSeat(conn, nextWl.getPassengerId(), racStatusId, null);
                        cancellationDAO.removeWaitlist(conn, nextWl.getWaitlistId());

                        RAC newRac = new RAC();
                        newRac.setPassengerId(nextWl.getPassengerId());
                        newRac.setBookingId(nextWl.getBookingId());
                        newRac.setRacNumber(newRacNumber);
                        newRac.setSeatId(null);
                        cancellationDAO.addToRAC(conn, newRac);
                    }
                }
            }

            // 3. Insert Cancellation record
            Cancellation cancellation = new Cancellation();
            cancellation.setBookingId(bookingId);
            cancellation.setCancelledBy(userId);
            cancellation.setCancellationReason(reason != null && !reason.trim().isEmpty() ? reason.trim() : "Customer Request");
            cancellation.setCancellationCharge(cancelCharge);
            cancellation.setRefundAmount(refundAmount);

            int cancelId = cancellationDAO.recordCancellation(conn, cancellation);
            if (cancelId <= 0) {
                throw new CancellationException("Failed to save cancellation record.");
            }
            cancellation.setCancellationId(cancelId);

            // 4. Insert Refund record if payment exists
            if (payment != null) {
                Refund refund = new Refund();
                refund.setPaymentId(payment.getPaymentId());
                refund.setRefundAmount(refundAmount);
                refund.setRefundReason("Ticket Cancellation - PNR: " + booking.getPnrNumber());
                refund.setRefundStatus("PROCESSED");
                paymentDAO.recordRefund(conn, refund);
            }

            // 5. Update Train Availability
            if (releasedSeatsCount > 0) {
                List<TrainCoach> coaches = coachDAO.getCoachesByTrainId(trainId);
                if (!coaches.isEmpty()) {
                    int coachId = coaches.get(0).getCoachId();
                    availabilityDAO.updateAvailability(conn, trainId, coachId, journeyDate, releasedSeatsCount, 0, 0);
                }
            }

            // Commit Transaction!
            conn.commit();

            cancellation.setPnrNumber(booking.getPnrNumber());
            return cancellation;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[CancellationService] Rollback error: " + rollbackEx.getMessage());
                }
            }
            throw new CancellationException("Cancellation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[CancellationService] Rollback error: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public Cancellation getCancellationDetails(int bookingId) {
        return cancellationDAO.getCancellationByBookingId(bookingId);
    }

    @Override
    public Refund getRefundDetails(int paymentId) {
        return paymentDAO.getRefundByPaymentId(paymentId);
    }

    @Override
    public List<Cancellation> getAllCancellations() {
        return cancellationDAO.getAllCancellations();
    }
}
