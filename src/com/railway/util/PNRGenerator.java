package com.railway.util;

import java.security.SecureRandom;

public class PNRGenerator {
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePNR() {
        // Standard 10-digit Indian Railways style PNR
        StringBuilder sb = new StringBuilder(10);
        // Start with non-zero digit
        sb.append(RANDOM.nextInt(9) + 1);
        for (int i = 1; i < 10; i++) {
            sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }
}
