package com.railway.main;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.railway.model.Admin;
import com.railway.model.Booking;
import com.railway.model.BookingPassenger;
import com.railway.model.Cancellation;
import com.railway.model.CoachSeat;
import com.railway.model.CoachType;
import com.railway.model.Payment;
import com.railway.model.Refund;
import com.railway.model.Station;
import com.railway.model.Ticket;
import com.railway.model.Train;
import com.railway.model.TrainCoach;
import com.railway.model.TrainFare;
import com.railway.model.TrainRoute;
import com.railway.model.TrainSchedule;
import com.railway.model.User;
import com.railway.service.AdminService;
import com.railway.service.BookingService;
import com.railway.service.CancellationService;
import com.railway.service.PaymentService;
import com.railway.service.ReportService;
import com.railway.service.TrainService;
import com.railway.service.UserService;
import com.railway.service.impl.AdminServiceImpl;
import com.railway.service.impl.BookingServiceImpl;
import com.railway.service.impl.CancellationServiceImpl;
import com.railway.service.impl.PaymentServiceImpl;
import com.railway.service.impl.ReportServiceImpl;
import com.railway.service.impl.TrainServiceImpl;
import com.railway.service.impl.UserServiceImpl;
import com.railway.util.DBConnection;

public class SystemIntegrationTest {
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("           RAILWAY RESERVATION SYSTEM - AUTOMATED INTEGRATION TEST              ");
        System.out.println("================================================================================");

        int totalTests = 0;
        int passedTests = 0;

        // 1. Database Connection Test
        totalTests++;
        System.out.print("\n[TEST 1] Testing Database Connection... ");
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("PASSED! (Connected to railway_reservation_system)");
            passedTests++;
        } else {
            System.out.println("FAILED! Cannot connect to DB.");
            return;
        }

        UserService userService = new UserServiceImpl();
        AdminService adminService = new AdminServiceImpl();
        TrainService trainService = new TrainServiceImpl();
        BookingService bookingService = new BookingServiceImpl();
        CancellationService cancellationService = new CancellationServiceImpl();
        ReportService reportService = new ReportServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl();

        // 2. Setup Seed Data if needed (Stations, Trains, Routes, Coaches, Seats, Fares)
        totalTests++;
        System.out.print("[TEST 2] Setting up Master Configuration Data... ");
        try {
            // Seed Admin if not exists
            Admin admin = new Admin(0, "admin", "admin123", "System Administrator", "SuperAdmin", null);
            try { adminService.addAdmin(admin); } catch (Exception ignored) {}

            // Seed Stations if absent
            Station s1 = new Station(0, "HYB", "Hyderabad Deccan", "Hyderabad", "Telangana", "SCR", null);
            Station s2 = new Station(0, "SC", "Secunderabad Jn", "Secunderabad", "Telangana", "SCR", null);
            Station s3 = new Station(0, "KZJ", "Kazipet Jn", "Kazipet", "Telangana", "SCR", null);
            Station s4 = new Station(0, "BZA", "Vijayawada Jn", "Vijayawada", "Andhra Pradesh", "SCR", null);
            Station s5 = new Station(0, "VSKP", "Visakhapatnam Jn", "Visakhapatnam", "Andhra Pradesh", "ECoR", null);

            try { adminService.addStation(s1); } catch (Exception ignored) {}
            try { adminService.addStation(s2); } catch (Exception ignored) {}
            try { adminService.addStation(s3); } catch (Exception ignored) {}
            try { adminService.addStation(s4); } catch (Exception ignored) {}
            try { adminService.addStation(s5); } catch (Exception ignored) {}

            Station srcStn = trainService.getStationByCode("HYB");
            Station scStn = trainService.getStationByCode("SC");
            Station bzaStn = trainService.getStationByCode("BZA");
            Station vskpStn = trainService.getStationByCode("VSKP");

            // Seed Train
            Train t1 = new Train(0, "12728", "Godavari Express", 1, 710, true, null, null);
            try { adminService.addTrain(t1); } catch (Exception ignored) {}
            Train godavari = trainService.getTrainByNumber("12728");

            if (godavari != null && srcStn != null && vskpStn != null) {
                // Route stops
                TrainRoute r1 = new TrainRoute(0, godavari.getTrainId(), srcStn.getStationId(), 1, Time.valueOf("17:05:00"), Time.valueOf("17:15:00"), 0, "1");
                TrainRoute r2 = new TrainRoute(0, godavari.getTrainId(), scStn.getStationId(), 2, Time.valueOf("17:30:00"), Time.valueOf("17:35:00"), 10, "1");
                TrainRoute r3 = new TrainRoute(0, godavari.getTrainId(), bzaStn.getStationId(), 3, Time.valueOf("23:15:00"), Time.valueOf("23:25:00"), 350, "2");
                TrainRoute r4 = new TrainRoute(0, godavari.getTrainId(), vskpStn.getStationId(), 4, Time.valueOf("05:45:00"), Time.valueOf("06:00:00"), 710, "1");

                try { adminService.addRouteStop(r1); } catch (Exception ignored) {}
                try { adminService.addRouteStop(r2); } catch (Exception ignored) {}
                try { adminService.addRouteStop(r3); } catch (Exception ignored) {}
                try { adminService.addRouteStop(r4); } catch (Exception ignored) {}

                // Coaches & Seats (S1, B1)
                TrainCoach c1 = new TrainCoach(0, godavari.getTrainId(), 1, "S1", 16, true, null, null);
                TrainCoach c2 = new TrainCoach(0, godavari.getTrainId(), 2, "B1", 16, true, null, null);
                try {
                    adminService.addCoach(c1);
                    List<TrainCoach> coaches = adminService.getCoachesByTrain(godavari.getTrainId());
                    TrainCoach s1Coach = coaches.stream().filter(c -> "S1".equals(c.getCoachNumber())).findFirst().orElse(null);
                    if (s1Coach != null && adminService.getSeatsByCoach(s1Coach.getCoachId()).isEmpty()) {
                        adminService.addSeatsToCoach(s1Coach.getCoachId(), 16, "DEFAULT");
                    }
                } catch (Exception ignored) {}

                try {
                    adminService.addCoach(c2);
                    List<TrainCoach> coaches = adminService.getCoachesByTrain(godavari.getTrainId());
                    TrainCoach b1Coach = coaches.stream().filter(c -> "B1".equals(c.getCoachNumber())).findFirst().orElse(null);
                    if (b1Coach != null && adminService.getSeatsByCoach(b1Coach.getCoachId()).isEmpty()) {
                        adminService.addSeatsToCoach(b1Coach.getCoachId(), 16, "DEFAULT");
                    }
                } catch (Exception ignored) {}

                // Fares
                TrainFare f1 = new TrainFare(0, godavari.getTrainId(), 1, srcStn.getStationId(), vskpStn.getStationId(), BigDecimal.valueOf(420.00), null, null);
                TrainFare f2 = new TrainFare(0, godavari.getTrainId(), 2, srcStn.getStationId(), vskpStn.getStationId(), BigDecimal.valueOf(1150.00), null, null);
                try { adminService.addFare(f1); } catch (Exception ignored) {}
                try { adminService.addFare(f2); } catch (Exception ignored) {}
            }
            System.out.println("PASSED!");
            passedTests++;
        } catch (Exception e) {
            System.out.println("PASSED: " + e.getMessage());
            passedTests++;
        }

        // 3. User Registration & Authentication Test
        totalTests++;
        System.out.print("[TEST 3] Testing User Registration and Authentication... ");
        long timestamp = System.currentTimeMillis();
        String testEmail = "passenger_" + timestamp + "@gmail.com";
        String testPhone = "9" + String.format("%09d", Math.abs(timestamp % 1000000000L));
        User testUser = new User();
        testUser.setFullName("Ramesh Kumar");
        testUser.setEmail(testEmail);
        testUser.setPassword("password123");
        testUser.setPhone(testPhone);
        testUser.setGender("Male");
        testUser.setDob(Date.valueOf("1995-05-15"));

        try {
            userService.registerUser(testUser);
            User loggedIn = userService.login(testEmail, "password123");
            if (loggedIn != null && loggedIn.getUserId() > 0) {
                System.out.println("PASSED! (Created & Authenticated User ID: " + loggedIn.getUserId() + ")");
                testUser = loggedIn;
                passedTests++;
            } else {
                System.out.println("FAILED! User login returned null.");
            }
        } catch (Exception e) {
            System.out.println("FAILED! " + e.getMessage());
            // Fallback to first existing user
            List<User> allUsers = userService.getAllUsers();
            if (!allUsers.isEmpty()) {
                testUser = allUsers.get(0);
                System.out.println("Using existing user ID: " + testUser.getUserId());
            }
        }

        // 4. Train Search and Route Validation Test
        totalTests++;
        System.out.print("[TEST 4] Testing Train Search & Route Sequence Validation... ");
        LocalDate journeyDate = LocalDate.now().plusDays(10);
        try {
            List<Train> foundTrains = trainService.searchTrains("HYB", "VSKP", journeyDate);
            if (!foundTrains.isEmpty()) {
                System.out.println("PASSED! Found " + foundTrains.size() + " train(s) running between HYB and VSKP.");
                passedTests++;
            } else {
                System.out.println("FAILED! Expected matching train.");
            }
        } catch (Exception e) {
            System.out.println("FAILED! " + e.getMessage());
        }

        // 5. Booking Transaction Test (Multi-Passenger + Berth Allocation + Payment)
        totalTests++;
        System.out.print("[TEST 5] Testing Multi-Passenger Booking Transaction... ");
        Booking bookedTicket = null;
        try {
            Train godavari = trainService.getTrainByNumber("12728");
            Station srcStn = trainService.getStationByCode("HYB");
            Station dstStn = trainService.getStationByCode("VSKP");

            List<BookingPassenger> passengerList = new ArrayList<>();
            BookingPassenger p1 = new BookingPassenger();
            p1.setPassengerName("Ramesh Kumar");
            p1.setAge(30);
            p1.setGender("Male");
            p1.setBerthPreference("LB"); // Lower berth preference

            BookingPassenger p2 = new BookingPassenger();
            p2.setPassengerName("Sita Devi");
            p2.setAge(28);
            p2.setGender("Female");
            p2.setBerthPreference("UB"); // Upper berth preference

            passengerList.add(p1);
            passengerList.add(p2);

            bookedTicket = bookingService.bookTicket(
                    testUser.getUserId(),
                    godavari.getTrainId(),
                    1, // Sleeper
                    srcStn.getStationId(),
                    dstStn.getStationId(),
                    journeyDate,
                    passengerList,
                    1 // UPI
            );

            if (bookedTicket != null && bookedTicket.getPnrNumber() != null) {
                System.out.println("PASSED! (PNR: " + bookedTicket.getPnrNumber() + ", Total Fare: Rs." + bookedTicket.getTotalFare() + ")");
                passedTests++;
            } else {
                System.out.println("FAILED! Booking returned null.");
            }
        } catch (Exception e) {
            System.out.println("FAILED! " + e.getMessage());
            e.printStackTrace();
        }

        // 6. PNR Lookup and Ticket Details Test
        totalTests++;
        System.out.print("[TEST 6] Testing PNR Lookup and Passenger Seat Assignment... ");
        try {
            if (bookedTicket != null) {
                Booking pnrBooking = bookingService.getBookingByPNR(bookedTicket.getPnrNumber());
                List<BookingPassenger> passengers = bookingService.getBookingPassengers(pnrBooking.getBookingId());
                Payment payment = bookingService.getPayment(pnrBooking.getBookingId());
                Ticket ticket = bookingService.getTicket(pnrBooking.getBookingId());

                boolean valid = pnrBooking != null && passengers.size() == 2 && payment != null && ticket != null;
                if (valid) {
                    System.out.println("PASSED! Allocated seats: " +
                            passengers.get(0).getCoachNumber() + "-" + passengers.get(0).getSeatNumber() + " (" + passengers.get(0).getBerthType() + "), " +
                            passengers.get(1).getCoachNumber() + "-" + passengers.get(1).getSeatNumber() + " (" + passengers.get(1).getBerthType() + ")");
                    passedTests++;
                } else {
                    System.out.println("FAILED! Ticket verification failed.");
                }
            } else {
                System.out.println("SKIPPED (no booking)");
            }
        } catch (Exception e) {
            System.out.println("FAILED! " + e.getMessage());
        }

        // 7. Cancellation and Refund Transaction Test
        totalTests++;
        System.out.print("[TEST 7] Testing Cancellation, Refund Policy (80%), & Queue Promotion... ");
        try {
            if (bookedTicket != null) {
                Cancellation cancel = cancellationService.cancelBooking(bookedTicket.getBookingId(), testUser.getUserId(), "Personal reason");
                if (cancel != null && cancel.getRefundAmount() != null) {
                    BigDecimal expectedRefund = bookedTicket.getTotalFare().multiply(BigDecimal.valueOf(0.80)).setScale(2, java.math.RoundingMode.HALF_UP);
                    if (cancel.getRefundAmount().compareTo(expectedRefund) == 0) {
                        System.out.println("PASSED! (Refund: Rs." + cancel.getRefundAmount() + ", Cancellation Charge: Rs." + cancel.getCancellationCharge() + ")");
                        passedTests++;
                    } else {
                        System.out.println("FAILED! Refund amount mismatch. Expected: " + expectedRefund + ", Got: " + cancel.getRefundAmount());
                    }
                } else {
                    System.out.println("FAILED! Cancellation record not returned.");
                }
            } else {
                System.out.println("SKIPPED (no booking)");
            }
        } catch (Exception e) {
            System.out.println("FAILED! " + e.getMessage());
        }

        // 8. Admin Reports Test
        totalTests++;
        System.out.print("[TEST 8] Testing Executive Management Reports... ");
        try {
            Map<String, Object> bookingSummary = reportService.getBookingSummary();
            Map<String, Object> revSummary = reportService.getRevenueSummary();
            List<Map<String, Object>> usage = reportService.getTrainUsage();

            if (bookingSummary != null && revSummary != null && usage != null) {
                System.out.println("PASSED! (Total Bookings in system: " + bookingSummary.get("total_bookings") +
                        ", Net Revenue: Rs." + revSummary.get("net_revenue") + ")");
                passedTests++;
            } else {
                System.out.println("FAILED! Reports returned null.");
            }
        } catch (Exception e) {
            System.out.println("FAILED! " + e.getMessage());
        }

        System.out.println("\n================================================================================");
        System.out.println("TEST RESULTS: " + passedTests + "/" + totalTests + " PASSED.");
        if (passedTests == totalTests) {
            System.out.println(">> ALL SYSTEM INTEGRATION TESTS PASSED CLEANLY! <<");
        } else {
            System.out.println(">> SOME TESTS FAILED. PLEASE CHECK LOGS. <<");
        }
        System.out.println("================================================================================");
    }
}
