package com.repinsky.copywise.converters;

import com.repinsky.copywise.dtos.AccountResponseDto;
import com.repinsky.copywise.models.Account;
import com.repinsky.copywise.models.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountConverterTest {
    private final AccountConverter accountConverter = new AccountConverter();

    @Test
    public void testEntityToDto() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber("5000014443748387");
        account.setAccountBalance(new BigDecimal("1000.50"));
        LocalDateTime createdAt = LocalDateTime.of(2025, 2, 17, 12, 0, 0);
        account.setCreatedAt(createdAt);

        AccountResponseDto dto = accountConverter.entityToDto(account);

        assertNotNull(dto);
        assertEquals("John", dto.getAccountOwnerFirstName());
        assertEquals("Doe", dto.getAccountOwnerLastName());
        assertEquals("john.doe@example.com", dto.getAccountOwnerEmail());
        assertEquals("5000014443748387", dto.getAccountNumber());
        assertEquals(new BigDecimal("1000.50"), dto.getBalance());
        assertEquals(createdAt, dto.getCreatedAt());
    }
}
