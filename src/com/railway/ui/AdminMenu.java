package com.railway.ui;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.railway.exception.RailwayException;
import com.railway.model.Admin;
import com.railway.model.AuditLog;
import com.railway.model.Booking;
import com.railway.model.Cancellation;
import com.railway.model.CoachSeat;
import com.railway.model.CoachType;
import com.railway.model.Payment;
import com.railway.model.Refund;
import com.railway.model.Station;
import com.railway.model.Train;
import com.railway.model.TrainCoach;
import com.railway.model.TrainFare;
import com.railway.model.TrainRoute;
import com.railway.model.TrainSchedule;
import com.railway.model.TrainType;
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
import com.railway.service.impl.ReportServiceImpl;
import com.railway.service.impl.TrainServiceImpl;
import com.railway.service.impl.UserServiceImpl;
import com.railway.util.InputValidator;

public class AdminMenu {
    private final Admin admin;
    private final AdminService adminService;
    private final TrainService trainService;
    private final BookingService bookingService;
    private final CancellationService cancellationService;
    private final UserService userService;
    private final ReportService reportService;

    public AdminMenu(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminServiceImpl();
        this.trainService = new TrainServiceImpl();
        this.bookingService = new BookingServiceImpl();
        this.cancellationService = new CancellationServiceImpl();
        this.userService = new UserServiceImpl();
        this.reportService = new ReportServiceImpl();
    }

    public void display() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n========================================================");
            System.out.println("          RAILWAY RESERVATION SYSTEM - ADMIN DASHBOARD  ");
            System.out.println("          Logged in as: " + admin.getFullName() + " (" + admin.getUsername() + ")");
            System.out.println("========================================================");
            System.out.println("   1. Manage Trains");
            System.out.println("   2. Manage Stations");
            System.out.println("   3. Manage Routes");
            System.out.println("   4. Manage Schedules");
            System.out.println("   5. Manage Coaches & Seats");
            System.out.println("   6. Manage Fares");
            System.out.println("   7. Manage Users");
            System.out.println("   8. View All Bookings");
            System.out.println("   9. View Payments & Refunds");
            System.out.println("  10. View Cancellations");
            System.out.println("  11. System Reports");
            System.out.println("  12. View Audit Logs");
            System.out.println("  13. Logout");
            System.out.println("========================================================");

            int choice = InputValidator.readIntInRange("Enter choice (1-13): ", 1, 13);
            switch (choice) {
                case 1 -> manageTrainsFlow();
                case 2 -> manageStationsFlow();
                case 3 -> manageRoutesFlow();
                case 4 -> manageSchedulesFlow();
                case 5 -> manageCoachesFlow();
                case 6 -> manageFaresFlow();
                case 7 -> manageUsersFlow();
                case 8 -> viewAllBookingsFlow();
                case 9 -> viewPaymentsFlow();
                case 10 -> viewCancellationsFlow();
                case 11 -> viewReportsFlow();
                case 12 -> viewAuditLogsFlow();
                case 13 -> {
                    System.out.println("Admin logged out successfully.");
                    exit = true;
                }
            }
        }
    }

    private void manageTrainsFlow() {
        System.out.println("\n--- MANAGE TRAINS ---");
        System.out.println("  1. List All Trains");
        System.out.println("  2. Add New Train");
        System.out.println("  3. Update Train Details");
        System.out.println("  4. Toggle Active Status");
        System.out.println("  5. Back");

        int choice = InputValidator.readIntInRange("Select option (1-5): ", 1, 5);
        switch (choice) {
            case 1 -> {
                List<Train> trains = adminService.getAllTrains();
                System.out.println("\n----------------------------------------------------------------------------------");
                System.out.printf("%-6s | %-10s | %-24s | %-14s | %-10s | %-8s%n",
                        "ID", "Number", "Train Name", "Type", "Distance", "Status");
                System.out.println("----------------------------------------------------------------------------------");
                for (Train t : trains) {
                    System.out.printf("%-6d | %-10s | %-24s | %-14s | %-10s | %-8s%n",
                            t.getTrainId(), t.getTrainNumber(), t.getTrainName(),
                            (t.getTrainTypeName() != null ? t.getTrainTypeName() : "N/A"),
                            (t.getTotalDistance() != null ? t.getTotalDistance() + " km" : "N/A"),
                            (t.isActive() ? "ACTIVE" : "INACTIVE"));
                }
                System.out.println("----------------------------------------------------------------------------------");
            }
            case 2 -> {
                String number = InputValidator.readNonEmptyString("Enter Train Number: ").trim();
                String name = InputValidator.readNonEmptyString("Enter Train Name: ").trim();
                List<CoachType> types = trainService.getAllCoachTypes();
                System.out.println("Train Types: 1. Express, 2. Superfast, 3. Rajdhani, 4. Shatabdi, 5. Duronto, 6. Garib Rath, 7. Passenger, 8. MEMU, 9. DEMU, 10. Vande Bharat");
                int typeId = InputValidator.readIntInRange("Select Train Type ID (1-10): ", 1, 10);
                int dist = InputValidator.readInt("Enter Total Route Distance in KM: ");

                Train train = new Train();
                train.setTrainNumber(number);
                train.setTrainName(name);
                train.setTrainTypeId(typeId);
                train.setTotalDistance(dist);
                train.setActive(true);

                try {
                    adminService.addTrain(train);
                    System.out.println("Train added successfully!");
                } catch (RailwayException e) {
                    System.out.println("Error adding train: " + e.getMessage());
                }
            }
            case 3 -> {
                String number = InputValidator.readNonEmptyString("Enter Train Number to edit: ").trim();
                Train train = trainService.getTrainByNumber(number);
                if (train == null) {
                    System.out.println("Train not found.");
                    return;
                }
                String name = InputValidator.readOptionalString("New Train Name", train.getTrainName());
                int dist = InputValidator.readInt("New Distance in KM: ");
                train.setTrainName(name);
                train.setTotalDistance(dist);

                try {
                    adminService.updateTrain(train);
                    System.out.println("Train updated successfully!");
                } catch (RailwayException e) {
                    System.out.println("Update error: " + e.getMessage());
                }
            }
            case 4 -> {
                String number = InputValidator.readNonEmptyString("Enter Train Number: ").trim();
                Train train = trainService.getTrainByNumber(number);
                if (train == null) {
                    System.out.println("Train not found.");
                    return;
                }
                boolean newStatus = !train.isActive();
                try {
                    adminService.setTrainActive(train.getTrainId(), newStatus);
                    System.out.println("Train status changed to: " + (newStatus ? "ACTIVE" : "INACTIVE"));
                } catch (RailwayException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private void manageStationsFlow() {
        System.out.println("\n--- MANAGE STATIONS ---");
        System.out.println("  1. List All Stations");
        System.out.println("  2. Add New Station");
        System.out.println("  3. Search Station");
        System.out.println("  4. Back");

        int choice = InputValidator.readIntInRange("Select option (1-4): ", 1, 4);
        switch (choice) {
            case 1 -> {
                List<Station> stations = adminService.getAllStations();
                System.out.println("\n----------------------------------------------------------------------------------");
                System.out.printf("%-6s | %-8s | %-24s | %-16s | %-16s | %-8s%n",
                        "ID", "Code", "Station Name", "City", "State", "Zone");
                System.out.println("----------------------------------------------------------------------------------");
                for (Station s : stations) {
                    System.out.printf("%-6d | %-8s | %-24s | %-16s | %-16s | %-8s%n",
                            s.getStationId(), s.getStationCode(), s.getStationName(), s.getCity(), s.getState(), (s.getZone() != null ? s.getZone() : "N/A"));
                }
                System.out.println("----------------------------------------------------------------------------------");
            }
            case 2 -> {
                String code = InputValidator.readNonEmptyString("Enter Station Code (e.g. HYB): ").toUpperCase().trim();
                String name = InputValidator.readNonEmptyString("Enter Station Name: ").trim();
                String city = InputValidator.readNonEmptyString("Enter City: ").trim();
                String state = InputValidator.readNonEmptyString("Enter State: ").trim();
                String zone = InputValidator.readOptionalString("Enter Zone (e.g. SCR, NR, SR)", "SCR");

                Station s = new Station(0, code, name, city, state, zone, null);
                try {
                    adminService.addStation(s);
                    System.out.println("Station added successfully!");
                } catch (RailwayException e) {
                    System.out.println("Error adding station: " + e.getMessage());
                }
            }
            case 3 -> {
                String query = InputValidator.readNonEmptyString("Enter search query (name/code/city): ");
                List<Station> list = trainService.searchStations(query);
                System.out.println("Found " + list.size() + " station(s):");
                for (Station s : list) {
                    System.out.printf("  [%s] %s - %s, %s%n", s.getStationCode(), s.getStationName(), s.getCity(), s.getState());
                }
            }
        }
    }

    private void manageRoutesFlow() {
        System.out.println("\n--- MANAGE ROUTES ---");
        String trainNo = InputValidator.readNonEmptyString("Enter Train Number: ");
        Train train = trainService.getTrainByNumber(trainNo);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        System.out.println("\nCurrent Route for " + train.getTrainName() + " (" + train.getTrainNumber() + "):");
        List<TrainRoute> routes = adminService.getRoutesByTrain(train.getTrainId());
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-6s | %-8s | %-20s | %-10s | %-10s | %-8s%n",
                "Stop", "ID", "Code", "Station Name", "Arrival", "Departure", "Dist(km)");
        System.out.println("----------------------------------------------------------------------------------");
        for (TrainRoute r : routes) {
            System.out.printf("%-4d | %-6d | %-8s | %-20s | %-10s | %-10s | %-8d%n",
                    r.getStopNumber(), r.getRouteId(), r.getStationCode(), r.getStationName(),
                    (r.getArrivalTime() != null ? r.getArrivalTime().toString() : "--"),
                    (r.getDepartureTime() != null ? r.getDepartureTime().toString() : "--"),
                    r.getDistanceFromSource());
        }
        System.out.println("----------------------------------------------------------------------------------");

        System.out.println("  1. Add Station Stop to Route");
        System.out.println("  2. Delete Stop from Route");
        System.out.println("  3. Back");
        int opt = InputValidator.readIntInRange("Choice (1-3): ", 1, 3);
        if (opt == 1) {
            String stnCode = InputValidator.readNonEmptyString("Enter Station Code: ").toUpperCase();
            Station stn = trainService.getStationByCode(stnCode);
            if (stn == null) {
                System.out.println("Station not found.");
                return;
            }
            int stopNo = InputValidator.readInt("Enter Stop Number: ");
            Time arr = InputValidator.readTime("Enter Arrival Time (HH:MM:SS)");
            Time dep = InputValidator.readTime("Enter Departure Time (HH:MM:SS)");
            int dist = InputValidator.readInt("Enter Distance from Source (km): ");
            String platform = InputValidator.readOptionalString("Platform Number", "1");

            TrainRoute r = new TrainRoute(0, train.getTrainId(), stn.getStationId(), stopNo, arr, dep, dist, platform);
            try {
                adminService.addRouteStop(r);
                System.out.println("Route stop added successfully!");
            } catch (RailwayException e) {
                System.out.println("Error adding stop: " + e.getMessage());
            }
        } else if (opt == 2) {
            int routeId = InputValidator.readInt("Enter Route Stop ID to delete: ");
            try {
                adminService.deleteRouteStop(routeId);
                System.out.println("Route stop deleted successfully!");
            } catch (RailwayException e) {
                System.out.println("Error deleting stop: " + e.getMessage());
            }
        }
    }

    private void manageSchedulesFlow() {
        System.out.println("\n--- MANAGE SCHEDULES ---");
        String trainNo = InputValidator.readNonEmptyString("Enter Train Number: ");
        Train train = trainService.getTrainByNumber(trainNo);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        LocalDate date = InputValidator.readFutureDate("Enter Journey Date");
        System.out.println("Status Options: 1. Running, 2. Delayed, 3. Cancelled");
        int statusChoice = InputValidator.readIntInRange("Select running status (1-3): ", 1, 3);
        String status = switch (statusChoice) {
            case 1 -> "Running";
            case 2 -> "Delayed";
            case 3 -> "Cancelled";
            default -> "Running";
        };

        int delay = 0;
        if ("Delayed".equals(status)) {
            delay = InputValidator.readInt("Enter delay in minutes: ");
        }

        TrainSchedule schedule = new TrainSchedule();
        schedule.setTrainId(train.getTrainId());
        schedule.setJourneyDate(Date.valueOf(date));
        schedule.setRunningStatus(status);
        schedule.setDelayMinutes(delay);

        try {
            adminService.addOrUpdateSchedule(schedule);
            System.out.println("Schedule updated successfully for " + train.getTrainName() + " on " + date + " [" + status + "]");
        } catch (RailwayException e) {
            System.out.println("Error updating schedule: " + e.getMessage());
        }
    }

    private void manageCoachesFlow() {
        System.out.println("\n--- MANAGE COACHES & SEATS ---");
        String trainNo = InputValidator.readNonEmptyString("Enter Train Number: ");
        Train train = trainService.getTrainByNumber(trainNo);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        List<TrainCoach> coaches = adminService.getCoachesByTrain(train.getTrainId());
        System.out.println("\nCurrent Coaches for " + train.getTrainName() + ":");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-12s | %-12s | %-8s%n", "ID", "Coach No", "Class", "Total Seats", "Status");
        System.out.println("----------------------------------------------------------------------------------");
        for (TrainCoach c : coaches) {
            System.out.printf("%-6d | %-10s | %-12s | %-12d | %-8s%n",
                    c.getCoachId(), c.getCoachNumber(), c.getCoachTypeName(), c.getTotalSeats(), (c.isActive() ? "ACTIVE" : "INACTIVE"));
        }
        System.out.println("----------------------------------------------------------------------------------");

        System.out.println("  1. Add Coach & Auto-Generate Seats");
        System.out.println("  2. View Seats for a Coach");
        System.out.println("  3. Back");

        int opt = InputValidator.readIntInRange("Choice (1-3): ", 1, 3);
        if (opt == 1) {
            String coachNo = InputValidator.readNonEmptyString("Enter Coach Identifier (e.g. S1, S2, B1, A1): ").toUpperCase();
            List<CoachType> types = trainService.getAllCoachTypes();
            System.out.println("Available Coach Types:");
            for (int i = 0; i < types.size(); i++) {
                System.out.println("  " + types.get(i).getCoachTypeId() + ". " + types.get(i).getCoachName() + " (" + types.get(i).getDescription() + ")");
            }
            int typeId = InputValidator.readIntInRange("Select Coach Type ID: ", 1, types.size());
            int totalSeats = InputValidator.readIntInRange("Enter Total Seats (e.g. 72, 64, 48): ", 1, 120);

            TrainCoach tc = new TrainCoach(0, train.getTrainId(), typeId, coachNo, totalSeats, true, null, null);
            try {
                adminService.addCoach(tc);
                List<TrainCoach> updated = adminService.getCoachesByTrain(train.getTrainId());
                TrainCoach created = updated.stream().filter(c -> coachNo.equals(c.getCoachNumber())).findFirst().orElse(null);
                if (created != null) {
                    adminService.addSeatsToCoach(created.getCoachId(), totalSeats, "DEFAULT");
                }
                System.out.println("Coach " + coachNo + " and " + totalSeats + " seats configured successfully!");
            } catch (RailwayException e) {
                System.out.println("Error adding coach: " + e.getMessage());
            }
        } else if (opt == 2) {
            int coachId = InputValidator.readInt("Enter Coach ID: ");
            List<CoachSeat> seats = adminService.getSeatsByCoach(coachId);
            System.out.println("Seats in Coach ID " + coachId + ":");
            for (int i = 0; i < seats.size(); i++) {
                CoachSeat s = seats.get(i);
                System.out.printf("[%2d-%s] ", s.getSeatNumber(), s.getBerthType());
                if ((i + 1) % 8 == 0) System.out.println();
            }
            System.out.println();
        }
    }

    private void manageFaresFlow() {
        System.out.println("\n--- MANAGE FARES ---");
        String trainNo = InputValidator.readNonEmptyString("Enter Train Number: ");
        Train train = trainService.getTrainByNumber(trainNo);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        List<TrainFare> fares = adminService.getFaresByTrain(train.getTrainId());
        System.out.println("\nConfigured Fares for " + train.getTrainName() + ":");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-18s | %-18s | %-10s%n", "Fare ID", "Class", "From", "To", "Amount");
        System.out.println("----------------------------------------------------------------------------------");
        for (TrainFare f : fares) {
            System.out.printf("%-6d | %-10s | %-18s | %-18s | Rs.%-8.2f%n",
                    f.getFareId(), f.getCoachTypeName(), f.getSourceStationName(), f.getDestinationStationName(), f.getFare());
        }
        System.out.println("----------------------------------------------------------------------------------");

        boolean add = InputValidator.readConfirmation("Do you want to set / update a fare?");
        if (add) {
            String srcCode = InputValidator.readNonEmptyString("Enter Source Station Code: ").toUpperCase();
            Station src = trainService.getStationByCode(srcCode);
            String dstCode = InputValidator.readNonEmptyString("Enter Destination Station Code: ").toUpperCase();
            Station dst = trainService.getStationByCode(dstCode);
            if (src == null || dst == null) {
                System.out.println("Invalid stations specified.");
                return;
            }

            int typeId = InputValidator.readIntInRange("Enter Coach Type ID (1-6): ", 1, 6);
            double amount = InputValidator.readDouble("Enter Fare Amount in Rs.: ");

            TrainFare tf = new TrainFare(0, train.getTrainId(), typeId, src.getStationId(), dst.getStationId(), BigDecimal.valueOf(amount), null, null);
            try {
                adminService.addFare(tf);
                System.out.println("Fare configured successfully!");
            } catch (RailwayException e) {
                System.out.println("Error adding fare: " + e.getMessage());
            }
        }
    }

    private void manageUsersFlow() {
        System.out.println("\n--- REGISTERED USERS ---");
        List<User> users = userService.getAllUsers();
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-20s | %-24s | %-14s | %-8s%n", "ID", "Full Name", "Email", "Phone", "Gender");
        System.out.println("----------------------------------------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-6d | %-20s | %-24s | %-14s | %-8s%n",
                    u.getUserId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getGender());
        }
        System.out.println("----------------------------------------------------------------------------------");
    }

    private void viewAllBookingsFlow() {
        System.out.println("\n--- ALL SYSTEM BOOKINGS ---");
        List<Booking> list = bookingService.getAllBookings();
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s | %-8s | %-20s | %-18s | %-12s | %-6s | %-10s | %-10s%n",
                "PNR", "Train No", "Customer", "Route", "Date", "Pass.", "Fare", "Status");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        for (Booking b : list) {
            System.out.printf("%-12s | %-8s | %-20s | %-18s | %-12s | %-6d | Rs.%-7.2f | %-10s%n",
                    b.getPnrNumber(), b.getTrainNumber(), truncate(b.getUserFullName(), 20),
                    b.getSourceStationCode() + "->" + b.getDestinationStationCode(),
                    b.getJourneyDate(), b.getTotalPassengers(), b.getTotalFare(), b.getStatusName());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }

    private void viewPaymentsFlow() {
        System.out.println("\n--- PAYMENTS & REFUNDS ---");
        PaymentService paymentService = new com.railway.service.impl.PaymentServiceImpl();
        List<Payment> payments = paymentService.getAllPayments();
        System.out.println("\nRecent Payments:");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-8s | %-18s | %-12s | %-10s | %-20s%n",
                "ID", "Book ID", "Transaction ID", "Amount", "Status", "Date");
        System.out.println("----------------------------------------------------------------------------------");
        for (Payment p : payments) {
            System.out.printf("%-6d | %-8d | %-18s | Rs.%-9.2f | %-10s | %-20s%n",
                    p.getPaymentId(), p.getBookingId(), p.getTransactionId(), p.getAmount(), p.getPaymentStatusName(), p.getPaymentDate());
        }
        System.out.println("----------------------------------------------------------------------------------");

        List<Refund> refunds = paymentService.getAllRefunds();
        System.out.println("\nRecent Refunds:");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-18s | %-10s | %-10s | %-20s%n",
                "ID", "PNR", "Transaction ID", "Refund", "Status", "Date");
        System.out.println("----------------------------------------------------------------------------------");
        for (Refund r : refunds) {
            System.out.printf("%-6d | %-10s | %-18s | Rs.%-7.2f | %-10s | %-20s%n",
                    r.getRefundId(), r.getPnrNumber(), r.getTransactionId(), r.getRefundAmount(), r.getRefundStatus(), r.getRefundDate());
        }
        System.out.println("----------------------------------------------------------------------------------");
    }

    private void viewCancellationsFlow() {
        System.out.println("\n--- CANCELLATION RECORDS ---");
        List<Cancellation> list = cancellationService.getAllCancellations();
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-12s | %-18s | %-10s | %-10s | %-20s%n",
                "ID", "PNR", "Cancelled By", "Charge", "Refund", "Date");
        System.out.println("----------------------------------------------------------------------------------");
        for (Cancellation c : list) {
            System.out.printf("%-6d | %-12s | %-18s | Rs.%-7.2f | Rs.%-7.2f | %-20s%n",
                    c.getCancellationId(), c.getPnrNumber(), truncate(c.getCancelledByUserName(), 18),
                    c.getCancellationCharge(), c.getRefundAmount(), c.getCancellationDate());
        }
        System.out.println("----------------------------------------------------------------------------------");
    }

    private void viewReportsFlow() {
        System.out.println("\n========================================================");
        System.out.println("                    EXECUTIVE REPORTS                   ");
        System.out.println("========================================================");

        Map<String, Object> summary = reportService.getBookingSummary();
        System.out.println("\n1. BOOKING STATUS SUMMARY:");
        System.out.println("   Total Bookings:    " + summary.getOrDefault("total_bookings", 0));
        System.out.println("   Confirmed Tickets: " + summary.getOrDefault("confirmed_count", 0));
        System.out.println("   RAC Tickets:       " + summary.getOrDefault("rac_count", 0));
        System.out.println("   Waitlist Tickets:  " + summary.getOrDefault("waitlist_count", 0));
        System.out.println("   Cancelled Tickets: " + summary.getOrDefault("cancelled_count", 0));

        Map<String, Object> rev = reportService.getRevenueSummary();
        System.out.println("\n2. REVENUE ANALYSIS:");
        System.out.printf("   Total Gross Booking Revenue: Rs. %.2f%n", rev.getOrDefault("total_revenue", BigDecimal.ZERO));
        System.out.printf("   Total Processed Refunds:     Rs. %.2f%n", rev.getOrDefault("total_refunds", BigDecimal.ZERO));
        System.out.printf("   Cancellation Charges Earned: Rs. %.2f%n", rev.getOrDefault("cancellation_charges", BigDecimal.ZERO));
        System.out.printf("   >> NET REVENUE:              Rs. %.2f%n", rev.getOrDefault("net_revenue", BigDecimal.ZERO));

        List<Map<String, Object>> trainUsage = reportService.getTrainUsage();
        System.out.println("\n3. TRAIN UTILIZATION REPORT:");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-10s | %-24s | %-14s | %-14s | %-12s%n",
                "Train No", "Train Name", "Total Bookings", "Passengers", "Revenue");
        System.out.println("----------------------------------------------------------------------------------");
        for (Map<String, Object> m : trainUsage) {
            System.out.printf("%-10s | %-24s | %-14s | %-14s | Rs.%-10.2f%n",
                    m.get("train_number"), truncate((String) m.get("train_name"), 24),
                    m.get("total_bookings"), m.get("total_passengers"), m.get("revenue"));
        }
        System.out.println("----------------------------------------------------------------------------------");
    }

    private void viewAuditLogsFlow() {
        System.out.println("\n--- AUDIT LOGS ---");
        List<AuditLog> logs = adminService.getAuditLogs(25);
        if (logs.isEmpty()) {
            System.out.println("No audit logs found.");
            return;
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-12s | %-16s | %-14s | %-10s | %-20s%n",
                "ID", "Admin", "Action", "Table", "Record ID", "Time");
        System.out.println("----------------------------------------------------------------------------------");
        for (AuditLog l : logs) {
            System.out.printf("%-6d | %-12s | %-16s | %-14s | %-10d | %-20s%n",
                    l.getLogId(), l.getAdminUsername(), l.getAction(), l.getTableName(), l.getRecordId(), l.getActionTime());
        }
        System.out.println("----------------------------------------------------------------------------------");
    }

    private String truncate(String str, int len) {
        if (str == null) return "";
        return str.length() > len ? str.substring(0, len - 2) + ".." : str;
    }
}
