package com.repinsky.copywise.services;

import com.repinsky.copywise.converters.AccountConverter;
import com.repinsky.copywise.dtos.AccountResponseDto;
import com.repinsky.copywise.exceptions.AccountNotFoundException;
import com.repinsky.copywise.exceptions.UserNotFoundException;
import com.repinsky.copywise.models.Account;
import com.repinsky.copywise.models.User;
import com.repinsky.copywise.repositories.AccountRepository;
import com.repinsky.copywise.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountManagementServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountConverter accountConverter;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountPersistenceService accountPersistenceService;

    @InjectMocks
    private AccountManagementService accountManagementService;

    private final String EMAIL = "user@example.com";

    @Test
    void getAllAccounts_Success() {
        // given
        when(userService.getCurrentUserEmail()).thenReturn(EMAIL);

        Account account1 = new Account();
        account1.setAccountNumber("5000014443748387");
        Account account2 = new Account();
        account2.setAccountNumber("5000015553748387");

        when(accountRepository.findAllAccounts(EMAIL))
                .thenReturn(Optional.of(List.of(account1, account2)));

        AccountResponseDto dto1 = new AccountResponseDto();
        dto1.setAccountNumber("5000014443748387");
        AccountResponseDto dto2 = new AccountResponseDto();
        dto2.setAccountNumber("5000015553748387");

        when(accountConverter.entityToDto(account1)).thenReturn(dto1);
        when(accountConverter.entityToDto(account2)).thenReturn(dto2);

        // when
        List<AccountResponseDto> result = accountManagementService.getAllAccounts();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("5000014443748387", result.get(0).getAccountNumber());
        assertEquals("5000015553748387", result.get(1).getAccountNumber());

        verify(userService, times(1)).getCurrentUserEmail();
        verify(accountRepository, times(1)).findAllAccounts(EMAIL);
        verify(accountConverter, times(1)).entityToDto(account1);
        verify(accountConverter, times(1)).entityToDto(account2);
    }

    @Test
    void getAllAccounts_NoAccountsFound_ShouldThrowException() {
        // given
        when(userService.getCurrentUserEmail()).thenReturn(EMAIL);
        when(accountRepository.findAllAccounts(EMAIL)).thenReturn(Optional.empty());

        // when + then
        assertThrows(AccountNotFoundException.class, () -> accountManagementService.getAllAccounts());
        verify(userService, times(1)).getCurrentUserEmail();
        verify(accountRepository, times(1)).findAllAccounts(EMAIL);
    }

    @Test
    void getAccount_Success() {
        // given
        String accountNumber = "5000015553748387";
        when(userService.getCurrentUserEmail()).thenReturn(EMAIL);

        Account account = new Account();
        account.setAccountNumber(accountNumber);

        when(accountRepository.findByAccountNumberAndUserEmail(accountNumber, EMAIL))
                .thenReturn(Optional.of(account));

        AccountResponseDto dto = new AccountResponseDto();
        dto.setAccountNumber(accountNumber);
        when(accountConverter.entityToDto(account)).thenReturn(dto);

        // when
        AccountResponseDto result = accountManagementService.getAccount(accountNumber);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        verify(userService, times(1)).getCurrentUserEmail();
        verify(accountRepository, times(1))
                .findByAccountNumberAndUserEmail(accountNumber, EMAIL);
        verify(accountConverter, times(1)).entityToDto(account);
    }

    @Test
    void getAccount_NotFound_ShouldThrowException() {
        // given
        String accountNumber = "5000015553748387";
        when(userService.getCurrentUserEmail()).thenReturn(EMAIL);
        when(accountRepository.findByAccountNumberAndUserEmail(accountNumber, EMAIL))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(AccountNotFoundException.class, () ->
                accountManagementService.getAccount(accountNumber));
        verify(userService, times(1)).getCurrentUserEmail();
        verify(accountRepository, times(1))
                .findByAccountNumberAndUserEmail(accountNumber, EMAIL);
    }

    @Test
    void createAccount_Success() {
        // given
        when(userService.getCurrentUserEmail()).thenReturn(EMAIL);

        User user = new User();
        user.setEmail(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);

        // when
        String result = accountManagementService.createAccount();

        // then
        assertEquals("Account created successfully", result);
        verify(accountPersistenceService, times(1)).saveAccount(any(Account.class));
    }

    @Test
    void createAccount_UserNotFound_ShouldThrowException() {
        // given
        when(userService.getCurrentUserEmail()).thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // when + then
        assertThrows(UserNotFoundException.class, () -> accountManagementService.createAccount());
        verify(accountPersistenceService, never()).saveAccount(any(Account.class));
    }
}
