package com.repinsky.copywise.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberGeneratorTest {
    @Test
    void testGenerateAccountNumber_LengthAndPrefix() {
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        assertNotNull(accountNumber, "Generated account number should not be null");

        assertEquals(16, accountNumber.length(), "Account number must be 16 digits long");

        assertTrue(accountNumber.startsWith("500001"),
                "Account number must start with '500001'");
    }

    @Test
    void testGenerateAccountNumber_LuhnCheckDigit() {
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        assertNotNull(accountNumber);

        String partialNumber = accountNumber.substring(0, accountNumber.length() - 1);
        int actualCheckDigit = Character.getNumericValue(accountNumber.charAt(accountNumber.length() - 1));

        int sum = 0;
        boolean alternate = true;
        for (int i = partialNumber.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(partialNumber.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        int expectedCheckDigit = (10 - (sum % 10)) % 10;

        assertEquals(expectedCheckDigit, actualCheckDigit,
                "Luhn check digit must match the expected one");
    }

    /**
     * Test for "randomness": checks that two calls in a row
     * usually do not give the same number (although theoretically
     * Random can give a collision).
     */
    @Test
    void testGenerateAccountNumber_Randomness() {
        String accountNumber1 = AccountNumberGenerator.generateAccountNumber();
        String accountNumber2 = AccountNumberGenerator.generateAccountNumber();

        assertNotEquals(accountNumber1, accountNumber2,
                "Two consecutive calls should produce different numbers (very rare collisions are possible)");
    }
}
