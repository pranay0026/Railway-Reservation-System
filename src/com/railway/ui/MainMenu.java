package com.railway.ui;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.railway.exception.AuthenticationException;
import com.railway.exception.RailwayException;
import com.railway.model.Admin;
import com.railway.model.Booking;
import com.railway.model.Station;
import com.railway.model.Train;
import com.railway.model.User;
import com.railway.service.AdminService;
import com.railway.service.BookingService;
import com.railway.service.TrainService;
import com.railway.service.UserService;
import com.railway.service.impl.AdminServiceImpl;
import com.railway.service.impl.BookingServiceImpl;
import com.railway.service.impl.TrainServiceImpl;
import com.railway.service.impl.UserServiceImpl;
import com.railway.util.InputValidator;

public class MainMenu {
    private final UserService userService;
    private final AdminService adminService;
    private final TrainService trainService;
    private final BookingService bookingService;

    public MainMenu() {
        this.userService = new UserServiceImpl();
        this.adminService = new AdminServiceImpl();
        this.trainService = new TrainServiceImpl();
        this.bookingService = new BookingServiceImpl();
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n================================================================================");
            System.out.println("                     RAILWAY RESERVATION MANAGEMENT SYSTEM                      ");
            System.out.println("                          Core Java + JDBC + MySQL                              ");
            System.out.println("================================================================================");
            System.out.println("  1. Passenger Login");
            System.out.println("  2. Passenger Registration");
            System.out.println("  3. Administrator Login");
            System.out.println("  4. Quick Train Search (Guest)");
            System.out.println("  5. Quick PNR Enquiry");
            System.out.println("  6. Exit Application");
            System.out.println("================================================================================");

            int choice = InputValidator.readIntInRange("Select an option (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> userLoginFlow();
                case 2 -> userRegisterFlow();
                case 3 -> adminLoginFlow();
                case 4 -> quickSearchFlow();
                case 5 -> quickPNRFlow();
                case 6 -> {
                    System.out.println("\nThank you for using the Railway Reservation System. Have a safe journey!");
                    exit = true;
                }
            }
        }
    }

    private void userLoginFlow() {
        System.out.println("\n--- PASSENGER LOGIN ---");
        String email = InputValidator.readEmail("Enter Email: ");
        String password = InputValidator.readNonEmptyString("Enter Password: ");

        try {
            User user = userService.login(email, password);
            System.out.println("\n>> Login Successful! Welcome, " + user.getFullName() + "! <<");
            UserMenu userMenu = new UserMenu(user);
            userMenu.display();
        } catch (AuthenticationException e) {
            System.out.println("\nAuthentication Error: " + e.getMessage());
        }
    }

    private void userRegisterFlow() {
        System.out.println("\n--- PASSENGER REGISTRATION ---");
        String fullName = InputValidator.readNonEmptyString("Enter Full Name: ");
        String email = InputValidator.readEmail("Enter Email Address: ");
        String password = InputValidator.readNonEmptyString("Create Password (min 4 characters): ");
        String phone = InputValidator.readPhone("Enter Phone Number (10 digits): ");
        String gender = InputValidator.readGender("Select Gender");
        LocalDate dob = InputValidator.readDate("Enter Date of Birth");

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setGender(gender);
        user.setDob(Date.valueOf(dob));

        try {
            boolean registered = userService.registerUser(user);
            if (registered) {
                System.out.println("\n>> REGISTRATION SUCCESSFUL! <<");
                System.out.println("You can now log in using your registered email: " + email);
            }
        } catch (RailwayException e) {
            System.out.println("\nRegistration Failed: " + e.getMessage());
        }
    }

    private void adminLoginFlow() {
        System.out.println("\n--- ADMINISTRATOR LOGIN ---");
        String username = InputValidator.readNonEmptyString("Enter Admin Username: ");
        String password = InputValidator.readNonEmptyString("Enter Admin Password: ");

        try {
            Admin admin = adminService.login(username, password);
            System.out.println("\n>> Admin Authentication Successful! Welcome, " + admin.getFullName() + "! <<");
            AdminMenu adminMenu = new AdminMenu(admin);
            adminMenu.display();
        } catch (AuthenticationException e) {
            System.out.println("\nAuthentication Error: " + e.getMessage());
        }
    }

    private void quickSearchFlow() {
        System.out.println("\n--- QUICK TRAIN SEARCH ---");
        String srcCode = InputValidator.readNonEmptyString("Enter Source Station Code (e.g. HYB, SC, VSKP, NDLS): ").toUpperCase();
        String dstCode = InputValidator.readNonEmptyString("Enter Destination Station Code (e.g. VSKP, BZA, SC): ").toUpperCase();
        LocalDate journeyDate = InputValidator.readFutureDate("Enter Journey Date");

        try {
            List<Train> trains = trainService.searchTrains(srcCode, dstCode, journeyDate);
            if (trains.isEmpty()) {
                System.out.println("\nNo trains found between " + srcCode + " and " + dstCode + " on " + journeyDate);
                return;
            }

            System.out.println("\nMatching Trains (" + trains.size() + "):");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.printf("%-8s | %-24s | %-14s | %-24s%n", "Train No", "Train Name", "Type", "Availability Preview");
            System.out.println("----------------------------------------------------------------------------------");

            for (Train t : trains) {
                Map<String, Integer> avail = trainService.getSeatAvailabilityByClass(t.getTrainId(), journeyDate);
                StringBuilder sb = new StringBuilder();
                avail.forEach((k, v) -> sb.append(k).append(":").append(v).append(" "));
                System.out.printf("%-8s | %-24s | %-14s | %-24s%n",
                        t.getTrainNumber(), t.getTrainName(),
                        (t.getTrainTypeName() != null ? t.getTrainTypeName() : "Express"),
                        sb.toString().trim());
            }
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Note: Please log in to your passenger account to book tickets.");

        } catch (RailwayException e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }

    private void quickPNRFlow() {
        System.out.println("\n--- QUICK PNR ENQUIRY ---");
        String pnr = InputValidator.readNonEmptyString("Enter 10-digit PNR: ").trim();
        try {
            Booking b = bookingService.getBookingByPNR(pnr);
            User tempUser = new User();
            tempUser.setUserId(b.getUserId());
            tempUser.setFullName(b.getUserFullName());
            UserMenu userMenu = new UserMenu(tempUser);
            userMenu.displayTicketCard(b);
        } catch (RailwayException e) {
            System.out.println("PNR Enquiry Error: " + e.getMessage());
        }
    }
}
