package com.railway.util;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static String readOptionalString(String prompt, String defaultValue) {
        System.out.print(prompt + (defaultValue != null ? " [" + defaultValue + "]: " : ": "));
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        return input;
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) {
                return val;
            }
            System.out.println("Value must be between " + min + " and " + max + ". Please try again.");
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val >= 0) {
                    return val;
                }
                System.out.println("Amount must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal number. Please try again.");
            }
        }
    }

    public static String readEmail(String prompt) {
        while (true) {
            String email = readNonEmptyString(prompt);
            if (EMAIL_PATTERN.matcher(email).matches()) {
                return email;
            }
            System.out.println("Invalid email format (e.g. user@example.com). Please try again.");
        }
    }

    public static String readPhone(String prompt) {
        while (true) {
            String phone = readNonEmptyString(prompt);
            if (PHONE_PATTERN.matcher(phone).matches()) {
                return phone;
            }
            System.out.println("Invalid phone number. Must be 10-15 digits. Please try again.");
        }
    }

    public static String readGender(String prompt) {
        while (true) {
            System.out.print(prompt + " (Male/Female/Other): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Male") || input.equalsIgnoreCase("M")) return "Male";
            if (input.equalsIgnoreCase("Female") || input.equalsIgnoreCase("F")) return "Female";
            if (input.equalsIgnoreCase("Other") || input.equalsIgnoreCase("O")) return "Other";
            System.out.println("Invalid gender. Please enter Male, Female, or Other.");
        }
    }

    public static String readBerthPreference(String prompt) {
        while (true) {
            System.out.print(prompt + " (LB/MB/UB/SL/SU/NA): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("LB") || input.equals("MB") || input.equals("UB") ||
                input.equals("SL") || input.equals("SU") || input.equals("NA") || input.isEmpty()) {
                return input.isEmpty() ? "NA" : input;
            }
            System.out.println("Invalid berth choice. Allowed: LB (Lower), MB (Middle), UB (Upper), SL (Side Lower), SU (Side Upper), NA (None).");
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD (e.g. 2026-08-20).");
            }
        }
    }

    public static LocalDate readFutureDate(String prompt) {
        while (true) {
            LocalDate date = readDate(prompt);
            if (!date.isBefore(LocalDate.now())) {
                return date;
            }
            System.out.println("Journey date must be today or in the future.");
        }
    }

    public static Time readTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (HH:MM:SS): ");
            String input = scanner.nextLine().trim();
            try {
                if (input.length() == 5) {
                    input = input + ":00";
                }
                return Time.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid time format. Please use HH:MM:SS (e.g. 14:30:00).");
            }
        }
    }

    public static boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("YES")) return true;
            if (input.equals("N") || input.equals("NO")) return false;
            System.out.println("Please enter Y or N.");
        }
    }
}
