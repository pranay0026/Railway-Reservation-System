package com.railway.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.railway.exception.BookingException;
import com.railway.exception.CancellationException;
import com.railway.exception.RailwayException;
import com.railway.model.Booking;
import com.railway.model.BookingPassenger;
import com.railway.model.CoachType;
import com.railway.model.Payment;
import com.railway.model.PaymentMethod;
import com.railway.model.Station;
import com.railway.model.Ticket;
import com.railway.model.Train;
import com.railway.model.TrainRoute;
import com.railway.model.User;
import com.railway.service.BookingService;
import com.railway.service.CancellationService;
import com.railway.service.TrainService;
import com.railway.service.UserService;
import com.railway.service.impl.BookingServiceImpl;
import com.railway.service.impl.CancellationServiceImpl;
import com.railway.service.impl.TrainServiceImpl;
import com.railway.service.impl.UserServiceImpl;
import com.railway.util.InputValidator;

public class UserMenu {
    private final User user;
    private final TrainService trainService;
    private final BookingService bookingService;
    private final CancellationService cancellationService;
    private final UserService userService;

    public UserMenu(User user) {
        this.user = user;
        this.trainService = new TrainServiceImpl();
        this.bookingService = new BookingServiceImpl();
        this.cancellationService = new CancellationServiceImpl();
        this.userService = new UserServiceImpl();
    }

    public void display() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n========================================================");
            System.out.println("          RAILWAY RESERVATION SYSTEM - USER PORTAL      ");
            System.out.println("          Welcome, " + user.getFullName() + " (" + user.getEmail() + ")");
            System.out.println("========================================================");
            System.out.println("  1. Search Trains");
            System.out.println("  2. Check Seat Availability");
            System.out.println("  3. Book Ticket");
            System.out.println("  4. View My Bookings");
            System.out.println("  5. View Ticket / PNR Status");
            System.out.println("  6. Cancel Ticket");
            System.out.println("  7. My Profile");
            System.out.println("  8. Logout");
            System.out.println("========================================================");

            int choice = InputValidator.readIntInRange("Enter choice (1-8): ", 1, 8);
            switch (choice) {
                case 1 -> searchTrainsFlow();
                case 2 -> checkAvailabilityFlow();
                case 3 -> bookTicketFlow();
                case 4 -> viewMyBookingsFlow();
                case 5 -> viewPNRStatusFlow();
                case 6 -> cancelTicketFlow();
                case 7 -> viewProfileFlow();
                case 8 -> {
                    System.out.println("Logging out... Goodbye!");
                    exit = true;
                }
            }
        }
    }

    private void searchTrainsFlow() {
        System.out.println("\n--- SEARCH TRAINS ---");
        String srcCode = InputValidator.readNonEmptyString("Enter Source Station Code (e.g. HYB, SC, VSKP, NDLS): ").toUpperCase();
        String dstCode = InputValidator.readNonEmptyString("Enter Destination Station Code (e.g. VSKP, BZA, SC): ").toUpperCase();
        LocalDate journeyDate = InputValidator.readFutureDate("Enter Journey Date");

        try {
            List<Train> trains = trainService.searchTrains(srcCode, dstCode, journeyDate);
            if (trains.isEmpty()) {
                System.out.println("\nNo matching trains found between " + srcCode + " and " + dstCode + " on " + journeyDate);
                return;
            }

            System.out.println("\nFound " + trains.size() + " train(s) running on " + journeyDate + ":");
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("%-8s | %-24s | %-12s | %-30s%n", "Train No", "Train Name", "Type", "Class Availability");
            System.out.println("--------------------------------------------------------------------------------------------------");

            for (Train t : trains) {
                Map<String, Integer> avail = trainService.getSeatAvailabilityByClass(t.getTrainId(), journeyDate);
                StringBuilder availStr = new StringBuilder();
                avail.forEach((k, v) -> availStr.append(k).append(":").append(v).append("  "));
                if (availStr.isEmpty()) availStr.append("Check Avail");

                System.out.printf("%-8s | %-24s | %-12s | %-30s%n",
                        t.getTrainNumber(),
                        t.getTrainName(),
                        (t.getTrainTypeName() != null ? t.getTrainTypeName() : "Express"),
                        availStr.toString().trim());
            }
            System.out.println("--------------------------------------------------------------------------------------------------");

        } catch (RailwayException e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }

    private void checkAvailabilityFlow() {
        System.out.println("\n--- CHECK SEAT AVAILABILITY ---");
        String trainNo = InputValidator.readNonEmptyString("Enter Train Number: ");
        Train train = trainService.getTrainByNumber(trainNo);
        if (train == null) {
            System.out.println("Train not found with number: " + trainNo);
            return;
        }

        LocalDate date = InputValidator.readFutureDate("Enter Journey Date");
        System.out.println("\nAvailable Classes for " + train.getTrainName() + " (" + train.getTrainNumber() + ") on " + date + ":");
        System.out.println("--------------------------------------------------");
        Map<String, Integer> avail = trainService.getSeatAvailabilityByClass(train.getTrainId(), date);
        if (avail.isEmpty()) {
            System.out.println("No coaches configured for this train.");
        } else {
            avail.forEach((k, v) -> System.out.printf("  %-10s -> Available Seats: %d%n", k, v));
        }
        System.out.println("--------------------------------------------------");
    }

    private void bookTicketFlow() {
        System.out.println("\n--- BOOK TICKET ---");
        String trainNo = InputValidator.readNonEmptyString("Enter Train Number: ");
        Train train = trainService.getTrainByNumber(trainNo);
        if (train == null || !train.isActive()) {
            System.out.println("Train not found or currently not active.");
            return;
        }

        String srcCode = InputValidator.readNonEmptyString("Enter Source Station Code: ").toUpperCase();
        Station src = trainService.getStationByCode(srcCode);
        if (src == null) {
            System.out.println("Source station not found: " + srcCode);
            return;
        }

        String dstCode = InputValidator.readNonEmptyString("Enter Destination Station Code: ").toUpperCase();
        Station dst = trainService.getStationByCode(dstCode);
        if (dst == null) {
            System.out.println("Destination station not found: " + dstCode);
            return;
        }

        if (src.getStationId() == dst.getStationId()) {
            System.out.println("Source and Destination cannot be the same.");
            return;
        }

        LocalDate journeyDate = InputValidator.readFutureDate("Enter Journey Date");

        // Select Coach Type
        List<CoachType> coachTypes = trainService.getAllCoachTypes();
        System.out.println("\nSelect Class / Coach Type:");
        for (int i = 0; i < coachTypes.size(); i++) {
            CoachType ct = coachTypes.get(i);
            int availSeats = trainService.getAvailableSeatsCount(train.getTrainId(), ct.getCoachTypeId(), journeyDate);
            BigDecimal fare = trainService.getFare(train.getTrainId(), ct.getCoachTypeId(), src.getStationId(), dst.getStationId());
            System.out.printf("  %d. %-5s (%-15s) | Available: %-4d | Fare: Rs. %.2f%n",
                    (i + 1), ct.getCoachName(), ct.getDescription(), availSeats, fare);
        }

        int classChoice = InputValidator.readIntInRange("Choose class (1-" + coachTypes.size() + "): ", 1, coachTypes.size());
        CoachType selectedCoachType = coachTypes.get(classChoice - 1);

        int numPassengers = InputValidator.readIntInRange("Enter number of passengers (1-6): ", 1, 6);
        List<BookingPassenger> passengers = new ArrayList<>();

        System.out.println("\nEnter Passenger Details:");
        for (int i = 1; i <= numPassengers; i++) {
            System.out.println("Passenger #" + i + ":");
            String name = InputValidator.readNonEmptyString("  Name: ");
            int age = InputValidator.readIntInRange("  Age: ", 1, 120);
            String gender = InputValidator.readGender("  Gender");
            String berthPref = InputValidator.readBerthPreference("  Berth Preference");

            BookingPassenger bp = new BookingPassenger();
            bp.setPassengerName(name);
            bp.setAge(age);
            bp.setGender(gender);
            bp.setBerthPreference(berthPref);
            passengers.add(bp);
        }

        BigDecimal perPassengerFare = trainService.getFare(train.getTrainId(), selectedCoachType.getCoachTypeId(), src.getStationId(), dst.getStationId());
        BigDecimal totalFare = perPassengerFare.multiply(BigDecimal.valueOf(numPassengers));

        System.out.println("\n================ BOOKING SUMMARY ================");
        System.out.println("Train: " + train.getTrainName() + " (" + train.getTrainNumber() + ")");
        System.out.println("Route: " + src.getStationName() + " (" + src.getStationCode() + ") -> " + dst.getStationName() + " (" + dst.getStationCode() + ")");
        System.out.println("Date: " + journeyDate);
        System.out.println("Class: " + selectedCoachType.getCoachName() + " - " + selectedCoachType.getDescription());
        System.out.println("Passengers: " + numPassengers);
        System.out.printf("Total Fare: Rs. %.2f%n", totalFare);
        System.out.println("=================================================");

        boolean confirm = InputValidator.readConfirmation("Proceed to payment?");
        if (!confirm) {
            System.out.println("Booking cancelled by user.");
            return;
        }

        // Payment Method selection
        System.out.println("\nSelect Payment Method:");
        System.out.println("  1. UPI");
        System.out.println("  2. Credit Card");
        System.out.println("  3. Debit Card");
        System.out.println("  4. Net Banking");
        int paymentChoice = InputValidator.readIntInRange("Select payment method (1-4): ", 1, 4);

        System.out.println("\nProcessing simulated payment...");
        try {
            Booking booked = bookingService.bookTicket(
                    user.getUserId(),
                    train.getTrainId(),
                    selectedCoachType.getCoachTypeId(),
                    src.getStationId(),
                    dst.getStationId(),
                    journeyDate,
                    passengers,
                    paymentChoice
            );

            System.out.println("\n>> PAYMENT SUCCESSFUL! <<");
            System.out.println(">> TICKET BOOKED SUCCESSFULLY! <<");
            displayTicketCard(booked);

        } catch (RailwayException e) {
            System.out.println("\nBooking Failed: " + e.getMessage());
        }
    }

    private void viewMyBookingsFlow() {
        System.out.println("\n--- MY BOOKINGS ---");
        List<Booking> list = bookingService.getUserBookings(user.getUserId());
        if (list.isEmpty()) {
            System.out.println("You have no booking history.");
            return;
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s | %-8s | %-20s | %-12s | %-16s | %-16s | %-6s | %-10s | %-10s%n",
                "PNR", "Train No", "Train Name", "Date", "From", "To", "Pass.", "Fare", "Status");
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        for (Booking b : list) {
            System.out.printf("%-12s | %-8s | %-20s | %-12s | %-16s | %-16s | %-6d | Rs.%-7.2f | %-10s%n",
                    b.getPnrNumber(),
                    b.getTrainNumber(),
                    truncate(b.getTrainName(), 20),
                    b.getJourneyDate(),
                    truncate(b.getSourceStationName(), 16),
                    truncate(b.getDestinationStationName(), 16),
                    b.getTotalPassengers(),
                    b.getTotalFare(),
                    b.getStatusName());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        String pnr = InputValidator.readOptionalString("Enter PNR to view ticket details (or press Enter to return)", "");
        if (!pnr.isEmpty()) {
            try {
                Booking b = bookingService.getBookingByPNR(pnr);
                displayTicketCard(b);
            } catch (RailwayException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void viewPNRStatusFlow() {
        System.out.println("\n--- VIEW TICKET / PNR STATUS ---");
        String pnr = InputValidator.readNonEmptyString("Enter 10-digit PNR Number: ").trim();
        try {
            Booking b = bookingService.getBookingByPNR(pnr);
            displayTicketCard(b);
        } catch (RailwayException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cancelTicketFlow() {
        System.out.println("\n--- CANCEL TICKET ---");
        String pnr = InputValidator.readNonEmptyString("Enter PNR Number to cancel: ").trim();
        try {
            Booking booking = bookingService.getBookingByPNR(pnr);
            if (booking.getUserId() != user.getUserId()) {
                System.out.println("Access Denied: You can only cancel tickets booked under your account.");
                return;
            }
            if ("CANCELLED".equalsIgnoreCase(booking.getStatusName())) {
                System.out.println("This ticket is already cancelled.");
                return;
            }

            BigDecimal refund = cancellationService.calculateRefundAmount(booking.getTotalFare(), booking.getStatusName());
            BigDecimal charge = cancellationService.calculateCancellationCharge(booking.getTotalFare(), booking.getStatusName());

            System.out.println("\n================ CANCELLATION PREVIEW ================");
            System.out.println("PNR: " + booking.getPnrNumber());
            System.out.println("Train: " + booking.getTrainName() + " (" + booking.getTrainNumber() + ")");
            System.out.println("Date: " + booking.getJourneyDate());
            System.out.println("Current Status: " + booking.getStatusName());
            System.out.printf("Total Fare Paid:       Rs. %.2f%n", booking.getTotalFare());
            System.out.printf("Cancellation Charge:   Rs. %.2f%n", charge);
            System.out.printf("Refund Amount (Credited): Rs. %.2f%n", refund);
            System.out.println("======================================================");

            boolean confirm = InputValidator.readConfirmation("Are you sure you want to cancel this ticket?");
            if (!confirm) {
                System.out.println("Cancellation aborted.");
                return;
            }

            String reason = InputValidator.readOptionalString("Enter cancellation reason", "Change of plans");
            cancellationService.cancelBooking(booking.getBookingId(), user.getUserId(), reason);

            System.out.println("\n>> TICKET CANCELLED SUCCESSFULLY! <<");
            System.out.printf(">> Refund of Rs. %.2f has been processed to your payment method. <<%n", refund);

        } catch (RailwayException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    private void viewProfileFlow() {
        System.out.println("\n--- MY PROFILE ---");
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Gender: " + user.getGender());
        System.out.println("Date of Birth: " + (user.getDob() != null ? user.getDob() : "N/A"));
        System.out.println("Account Created: " + user.getCreatedAt());

        boolean edit = InputValidator.readConfirmation("\nDo you want to update your details?");
        if (edit) {
            String name = InputValidator.readOptionalString("New Full Name", user.getFullName());
            String phone = InputValidator.readOptionalString("New Phone", user.getPhone());
            String gender = InputValidator.readGender("New Gender");

            user.setFullName(name);
            user.setPhone(phone);
            user.setGender(gender);
            try {
                userService.updateProfile(user);
                System.out.println("Profile updated successfully!");
            } catch (RailwayException e) {
                System.out.println("Update failed: " + e.getMessage());
            }
        }
    }

    public void displayTicketCard(Booking b) {
        List<BookingPassenger> passengers = bookingService.getBookingPassengers(b.getBookingId());
        Payment payment = bookingService.getPayment(b.getBookingId());
        Ticket ticket = bookingService.getTicket(b.getBookingId());

        System.out.println("\n================================================================================");
        System.out.println("                           INDIAN RAILWAYS E-TICKET                             ");
        System.out.println("================================================================================");
        System.out.printf("  PNR: %-16s | Ticket No: %-16s | Status: %-12s%n",
                b.getPnrNumber(), (ticket != null ? ticket.getTicketNumber() : "N/A"), b.getStatusName());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("  Train: %-26s | Train No: %-10s%n", b.getTrainName(), b.getTrainNumber());
        System.out.printf("  From:  %-26s | To:       %-26s%n",
                b.getSourceStationName() + " (" + b.getSourceStationCode() + ")",
                b.getDestinationStationName() + " (" + b.getDestinationStationCode() + ")");
        System.out.printf("  Journey Date: %-18s | Booked On: %-20s%n",
                b.getJourneyDate(), b.getBookingDate());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("  PASSENGER DETAILS:");
        System.out.printf("  %-4s | %-22s | %-4s | %-7s | %-12s | %-12s%n",
                "#", "Passenger Name", "Age", "Gender", "Status", "Seat / Berth");
        System.out.println("  ----------------------------------------------------------------------------");

        for (int i = 0; i < passengers.size(); i++) {
            BookingPassenger p = passengers.get(i);
            String seatInfo;
            if ("CONFIRMED".equalsIgnoreCase(p.getStatusName()) && p.getSeatNumber() != null) {
                seatInfo = p.getCoachNumber() + "-" + p.getSeatNumber() + " (" + p.getBerthType() + ")";
            } else if ("RAC".equalsIgnoreCase(p.getStatusName())) {
                seatInfo = "RAC " + (p.getRacNumber() != null ? p.getRacNumber() : "");
            } else if ("WAITLIST".equalsIgnoreCase(p.getStatusName())) {
                seatInfo = "WL " + (p.getWaitlistNumber() != null ? p.getWaitlistNumber() : "");
            } else {
                seatInfo = p.getStatusName();
            }

            System.out.printf("  %-4d | %-22s | %-4d | %-7s | %-12s | %-12s%n",
                    (i + 1),
                    truncate(p.getPassengerName(), 22),
                    p.getAge(),
                    p.getGender(),
                    p.getStatusName(),
                    seatInfo);
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("  Total Passengers: %-6d | Total Fare: Rs. %-10.2f%n",
                b.getTotalPassengers(), b.getTotalFare());
        if (payment != null) {
            System.out.printf("  Payment Status:   %-12s | Txn ID:    %-24s%n",
                    payment.getPaymentStatusName(), payment.getTransactionId());
        }
        System.out.println("================================================================================\n");
    }

    private String truncate(String str, int len) {
        if (str == null) return "";
        return str.length() > len ? str.substring(0, len - 2) + ".." : str;
    }
}
