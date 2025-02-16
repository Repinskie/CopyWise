package com.repinsky.copywise.services;

import com.repinsky.copywise.exceptions.AccountNotFoundException;
import com.repinsky.copywise.exceptions.IllegalArgumentException;
import com.repinsky.copywise.exceptions.InsufficientFundsException;
import com.repinsky.copywise.models.Account;
import com.repinsky.copywise.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountTransactionServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountPersistenceService accountPersistenceService;

    @InjectMocks
    private AccountTransactionService accountTransactionService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNumber("5000015553748387");
        account.setAccountBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void testDoTheDeposit_ValidAmount() {
        BigDecimal depositAmount = BigDecimal.valueOf(500);
        when(accountRepository.findByAccountNumber("5000015553748387")).thenReturn(Optional.of(account));

        String result = accountTransactionService.doTheDeposit("5000015553748387", depositAmount);

        BigDecimal expectedBalance = BigDecimal.valueOf(1500);
        assertEquals(expectedBalance, account.getAccountBalance());
        assertTrue(result.contains(expectedBalance.toString()));
        verify(accountPersistenceService, times(1)).saveAccount(account);
    }

    @Test
    void testDoTheDeposit_InvalidAmount_Zero() {
        BigDecimal depositAmount = BigDecimal.ZERO;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                accountTransactionService.doTheDeposit("5000015553748387", depositAmount)
        );
        assertTrue(exception.getMessage().contains("Deposit amount must be greater than zero"));
    }

    @Test
    void testDoTheDeposit_AccountNotFound() {
        BigDecimal depositAmount = BigDecimal.valueOf(500);
        when(accountRepository.findByAccountNumber("5000015553748387")).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                accountTransactionService.doTheDeposit("5000015553748387", depositAmount)
        );
        assertTrue(exception.getMessage().contains("Account with number '5000015553748387' not found"));
    }

    @Test
    void testDoTheWithdraw_ValidAmount() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(200);
        // предполагаем, что email текущего пользователя: "user@example.com"
        when(userService.getCurrentUserEmail()).thenReturn("user@example.com");
        when(accountRepository.findByAccountNumberAndUserEmail("5000015553748387", "user@example.com"))
                .thenReturn(Optional.of(account));

        String result = accountTransactionService.doTheWithdraw("5000015553748387", withdrawAmount);

        BigDecimal expectedBalance = BigDecimal.valueOf(800);
        assertEquals(expectedBalance, account.getAccountBalance());
        assertTrue(result.contains(expectedBalance.toString()));
        verify(accountPersistenceService, times(1)).saveAccount(account);
    }

    @Test
    void testDoTheWithdraw_InsufficientFunds() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(1200);
        when(userService.getCurrentUserEmail()).thenReturn("user@example.com");
        when(accountRepository.findByAccountNumberAndUserEmail("5000015553748387", "user@example.com"))
                .thenReturn(Optional.of(account));

        Exception exception = assertThrows(InsufficientFundsException.class, () ->
                accountTransactionService.doTheWithdraw("5000015553748387", withdrawAmount)
        );
        assertTrue(exception.getMessage().contains("Not enough funds on account: 5000015553748387"));
    }

    @Test
    void testDoTheWithdraw_InvalidAmount() {
        BigDecimal withdrawAmount = BigDecimal.ZERO;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                accountTransactionService.doTheWithdraw("5000015553748387", withdrawAmount)
        );
        assertTrue(exception.getMessage().contains("Withdrawal amount must be greater than zero"));
    }

    @Test
    void testToTheTransfer_ValidAmount() {
        BigDecimal transferAmount = BigDecimal.valueOf(300);

        Account senderAccount = new Account();
        senderAccount.setAccountNumber("5000015765168387");
        senderAccount.setAccountBalance(BigDecimal.valueOf(1000));

        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("5000019876548387");
        receiverAccount.setAccountBalance(BigDecimal.valueOf(500));

        when(userService.getCurrentUserEmail()).thenReturn("user@example.com");
        when(accountRepository.findByAccountNumberAndUserEmail("5000015765168387", "user@example.com"))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber("5000019876548387"))
                .thenReturn(Optional.of(receiverAccount));

        String result = accountTransactionService.toTheTransfer("5000015765168387", "5000019876548387", transferAmount);

        BigDecimal expectedSenderBalance = BigDecimal.valueOf(700);
        BigDecimal expectedReceiverBalance = BigDecimal.valueOf(800);
        assertEquals(expectedSenderBalance, senderAccount.getAccountBalance());
        assertEquals(expectedReceiverBalance, receiverAccount.getAccountBalance());
        assertTrue(result.contains("successfully processed"));
        verify(accountPersistenceService, times(1)).saveAccount(senderAccount);
        verify(accountPersistenceService, times(1)).saveAccount(receiverAccount);
    }

    @Test
    void testToTheTransfer_InvalidAmount() {
        BigDecimal transferAmount = BigDecimal.ZERO;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                accountTransactionService.toTheTransfer("5000015765168387", "5000019876548387", transferAmount)
        );
        assertTrue(exception.getMessage().contains("Transfer amount must be greater than zero"));
    }
}
