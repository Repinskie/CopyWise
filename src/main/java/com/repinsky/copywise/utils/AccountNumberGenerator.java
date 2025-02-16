package com.repinsky.copywise.utils;

import java.util.Random;

public class AccountNumberGenerator {
    private static final String AIN = "500001"; // account identifier number

    public static String generateAccountNumber() {
        String uniquePart = generateRandomDigits(9);
        String partialNumber = AIN + uniquePart;
        int checkDigit = calculateLuhnCheckDigit(partialNumber);
        return partialNumber + checkDigit;
    }

    private static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
}
