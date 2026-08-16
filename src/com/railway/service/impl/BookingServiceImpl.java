package com.railway.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.railway.dao.AvailabilityDAO;
import com.railway.dao.BookingDAO;
import com.railway.dao.CancellationDAO;
import com.railway.dao.CoachDAO;
import com.railway.dao.FareDAO;
import com.railway.dao.LookupDAO;
import com.railway.dao.PaymentDAO;
import com.railway.dao.SeatDAO;
import com.railway.dao.TrainDAO;
import com.railway.dao.impl.AvailabilityDAOImpl;
import com.railway.dao.impl.BookingDAOImpl;
import com.railway.dao.impl.CancellationDAOImpl;
import com.railway.dao.impl.CoachDAOImpl;
import com.railway.dao.impl.FareDAOImpl;
import com.railway.dao.impl.LookupDAOImpl;
import com.railway.dao.impl.PaymentDAOImpl;
import com.railway.dao.impl.SeatDAOImpl;
import com.railway.dao.impl.TrainDAOImpl;
import com.railway.exception.BookingException;
import com.railway.exception.PaymentException;
import com.railway.exception.RailwayException;
import com.railway.exception.SeatUnavailableException;
import com.railway.model.Booking;
import com.railway.model.BookingPassenger;
import com.railway.model.CoachSeat;
import com.railway.model.Payment;
import com.railway.model.RAC;
import com.railway.model.Ticket;
import com.railway.model.Train;
import com.railway.model.TrainCoach;
import com.railway.model.Waitlist;
import com.railway.service.BookingService;
import com.railway.util.DBConnection;
import com.railway.util.PNRGenerator;
import com.railway.util.TransactionIdGenerator;

public class BookingServiceImpl implements BookingService {
    private final BookingDAO bookingDAO;
    private final PaymentDAO paymentDAO;
    private final SeatDAO seatDAO;
    private final AvailabilityDAO availabilityDAO;
    private final FareDAO fareDAO;
    private final LookupDAO lookupDAO;
    private final CancellationDAO cancellationDAO;
    private final TrainDAO trainDAO;
    private final CoachDAO coachDAO;

    public BookingServiceImpl() {
        this.bookingDAO = new BookingDAOImpl();
        this.paymentDAO = new PaymentDAOImpl();
        this.seatDAO = new SeatDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.fareDAO = new FareDAOImpl();
        this.lookupDAO = new LookupDAOImpl();
        this.cancellationDAO = new CancellationDAOImpl();
        this.trainDAO = new TrainDAOImpl();
        this.coachDAO = new CoachDAOImpl();
    }

    @Override
    public Booking bookTicket(int userId, int trainId, int coachTypeId, int sourceStationId, int destinationStationId,
                              LocalDate journeyDate, List<BookingPassenger> passengers, int paymentMethodId)
            throws BookingException, SeatUnavailableException, PaymentException, RailwayException {

        if (passengers == null || passengers.isEmpty()) {
            throw new BookingException("At least one passenger is required for booking.");
        }
        if (passengers.size() > 6) {
            throw new BookingException("Maximum 6 passengers allowed per booking.");
        }
        if (journeyDate == null || journeyDate.isBefore(LocalDate.now())) {
            throw new BookingException("Journey date must be today or in the future.");
        }

        Train train = trainDAO.getTrainById(trainId);
        if (train == null || !train.isActive()) {
            throw new BookingException("Selected train is not active or does not exist.");
        }

        Date sqlJourneyDate = Date.valueOf(journeyDate);

        // Fetch lookup status IDs
        int confirmedStatusId = lookupDAO.getBookingStatusId("CONFIRMED");
        int racStatusId = lookupDAO.getBookingStatusId("RAC");
        int waitlistStatusId = lookupDAO.getBookingStatusId("WAITLIST");
        int paymentSuccessStatusId = lookupDAO.getPaymentStatusId("SUCCESS");

        if (confirmedStatusId <= 0 || racStatusId <= 0 || waitlistStatusId <= 0) {
            throw new BookingException("System configuration error: Booking status lookups missing.");
        }

        // Calculate single passenger fare
        BigDecimal baseFare = fareDAO.calculateFare(trainId, coachTypeId, sourceStationId, destinationStationId);
        BigDecimal totalFare = baseFare.multiply(BigDecimal.valueOf(passengers.size()));

        // Start Transaction
        Connection conn = null;
        try {
            conn = DBConnection.getNewConnection();
            conn.setAutoCommit(false);

            // Fetch currently unallocated seats for the train, coach type, and journey date
            List<CoachSeat> availableSeats = seatDAO.getAvailableSeatsForTrainAndType(trainId, coachTypeId, sqlJourneyDate);
            Set<Integer> allocatedSeatIdsInThisTx = new HashSet<>();

            int confirmedCount = 0;
            int racCount = 0;
            int waitlistCount = 0;

            for (BookingPassenger passenger : passengers) {
                if (passenger.getPassengerName() == null || passenger.getPassengerName().trim().isEmpty()) {
                    throw new BookingException("Passenger name cannot be empty.");
                }
                if (passenger.getAge() <= 0 || passenger.getAge() > 125) {
                    throw new BookingException("Invalid age for passenger " + passenger.getPassengerName());
                }

                String pref = passenger.getBerthPreference() != null ? passenger.getBerthPreference().toUpperCase() : "NA";
                CoachSeat allocatedSeat = null;

                // 1. Try to find a seat matching the berth preference
                if (!"NA".equals(pref)) {
                    for (CoachSeat seat : availableSeats) {
                        if (!allocatedSeatIdsInThisTx.contains(seat.getSeatId()) && pref.equalsIgnoreCase(seat.getBerthType())) {
                            allocatedSeat = seat;
                            break;
                        }
                    }
                }

                // 2. If no matching preference, allocate any available seat
                if (allocatedSeat == null) {
                    for (CoachSeat seat : availableSeats) {
                        if (!allocatedSeatIdsInThisTx.contains(seat.getSeatId())) {
                            allocatedSeat = seat;
                            break;
                        }
                    }
                }

                // 3. If a seat is found -> CONFIRMED
                if (allocatedSeat != null) {
                    allocatedSeatIdsInThisTx.add(allocatedSeat.getSeatId());
                    passenger.setSeatId(allocatedSeat.getSeatId());
                    passenger.setBookingStatusId(confirmedStatusId);
                    passenger.setSeatNumber(allocatedSeat.getSeatNumber());
                    passenger.setCoachNumber(allocatedSeat.getCoachNumber());
                    passenger.setBerthType(allocatedSeat.getBerthType());
                    confirmedCount++;
                } else {
                    // 4. If confirmed seats full, check RAC capacity (max 20 RAC per train coach type)
                    int currentRAC = cancellationDAO.getNextRACNumber(conn, trainId, sqlJourneyDate);
                    if (currentRAC <= 20) {
                        passenger.setSeatId(null);
                        passenger.setBookingStatusId(racStatusId);
                        passenger.setRacNumber(currentRAC);
                        racCount++;
                    } else {
                        // 5. Assign to Waitlist
                        int currentWL = cancellationDAO.getNextWaitlistNumber(conn, trainId, sqlJourneyDate);
                        passenger.setSeatId(null);
                        passenger.setBookingStatusId(waitlistStatusId);
                        passenger.setWaitlistNumber(currentWL);
                        waitlistCount++;
                    }
                }
            }

            // Overall Booking Status
            int overallBookingStatusId;
            if (confirmedCount > 0) {
                overallBookingStatusId = confirmedStatusId;
            } else if (racCount > 0) {
                overallBookingStatusId = racStatusId;
            } else {
                overallBookingStatusId = waitlistStatusId;
            }

            // Generate unique PNR
            String pnr;
            do {
                pnr = PNRGenerator.generatePNR();
            } while (bookingDAO.getBookingByPNR(pnr) != null);

            // Create Booking Record
            Booking booking = new Booking();
            booking.setPnrNumber(pnr);
            booking.setUserId(userId);
            booking.setTrainId(trainId);
            booking.setSourceStationId(sourceStationId);
            booking.setDestinationStationId(destinationStationId);
            booking.setJourneyDate(sqlJourneyDate);
            booking.setTotalPassengers(passengers.size());
            booking.setTotalFare(totalFare);
            booking.setBookingStatusId(overallBookingStatusId);

            int bookingId = bookingDAO.createBooking(conn, booking);
            if (bookingId <= 0) {
                throw new BookingException("Failed to create booking header record.");
            }
            booking.setBookingId(bookingId);

            // Insert Passengers and RAC/Waitlist records
            for (BookingPassenger p : passengers) {
                p.setBookingId(bookingId);
                int passengerId = bookingDAO.addBookingPassenger(conn, p);
                if (passengerId <= 0) {
                    throw new BookingException("Failed to record passenger: " + p.getPassengerName());
                }
                p.setPassengerId(passengerId);

                if (p.getBookingStatusId() == racStatusId) {
                    RAC rac = new RAC();
                    rac.setPassengerId(passengerId);
                    rac.setBookingId(bookingId);
                    rac.setRacNumber(p.getRacNumber());
                    rac.setSeatId(null);
                    cancellationDAO.addToRAC(conn, rac);
                } else if (p.getBookingStatusId() == waitlistStatusId) {
                    Waitlist wl = new Waitlist();
                    wl.setPassengerId(passengerId);
                    wl.setBookingId(bookingId);
                    wl.setWaitlistNumber(p.getWaitlistNumber());
                    cancellationDAO.addToWaitlist(conn, wl);
                }
            }

            // Generate and Insert Ticket
            Ticket ticket = new Ticket();
            ticket.setBookingId(bookingId);
            ticket.setTicketNumber("TKT-" + pnr);
            ticket.setQrCode("QR-" + pnr);
            ticket.setTicketPdf("TICKET_" + pnr + ".pdf");
            boolean ticketCreated = bookingDAO.createTicket(conn, ticket);
            if (!ticketCreated) {
                throw new BookingException("Failed to generate ticket record.");
            }

            // Process Payment Simulation
            String txnId = TransactionIdGenerator.generateTransactionId();
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setPaymentMethodId(paymentMethodId > 0 ? paymentMethodId : 1);
            payment.setPaymentStatusId(paymentSuccessStatusId > 0 ? paymentSuccessStatusId : 1);
            payment.setTransactionId(txnId);
            payment.setAmount(totalFare);

            int paymentId = paymentDAO.recordPayment(conn, payment);
            if (paymentId <= 0) {
                throw new PaymentException("Payment recording failed. Transaction aborted.");
            }

            // Update Train Availability counters
            List<TrainCoach> coaches = coachDAO.getCoachesByTrainAndType(trainId, coachTypeId);
            if (!coaches.isEmpty()) {
                int firstCoachId = coaches.get(0).getCoachId();
                availabilityDAO.initializeAvailabilityIfAbsent(conn, trainId, firstCoachId, sqlJourneyDate, coaches.get(0).getTotalSeats());
                availabilityDAO.updateAvailability(conn, trainId, firstCoachId, sqlJourneyDate, -confirmedCount, racCount, waitlistCount);
            }

            // Commit Transaction!
            conn.commit();

            return getBookingByPNR(pnr);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[BookingService] Rollback error: " + rollbackEx.getMessage());
                }
            }
            throw new BookingException("Booking transaction failed: " + e.getMessage(), e);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[BookingService] Rollback error: " + rollbackEx.getMessage());
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
    public Booking getBookingByPNR(String pnr) throws RailwayException {
        if (pnr == null || pnr.trim().isEmpty()) {
            throw new RailwayException("PNR cannot be empty.");
        }
        Booking b = bookingDAO.getBookingByPNR(pnr.trim());
        if (b == null) {
            throw new RailwayException("No booking found for PNR: " + pnr);
        }
        return b;
    }

    @Override
    public Booking getBookingById(int bookingId) throws RailwayException {
        Booking b = bookingDAO.getBookingById(bookingId);
        if (b == null) {
            throw new RailwayException("No booking found with ID: " + bookingId);
        }
        return b;
    }

    @Override
    public List<Booking> getUserBookings(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    @Override
    public List<BookingPassenger> getBookingPassengers(int bookingId) {
        return bookingDAO.getPassengersByBookingId(bookingId);
    }

    @Override
    public Ticket getTicket(int bookingId) {
        return bookingDAO.getTicketByBookingId(bookingId);
    }

    @Override
    public Payment getPayment(int bookingId) {
        return paymentDAO.getPaymentByBookingId(bookingId);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
}
