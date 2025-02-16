package com.repinsky.copywise.services;

import com.repinsky.copywise.converters.AccountConverter;
import com.repinsky.copywise.dtos.AccountResponseDto;
import com.repinsky.copywise.exceptions.AccountNotFoundException;
import com.repinsky.copywise.exceptions.UserNotFoundException;
import com.repinsky.copywise.models.Account;
import com.repinsky.copywise.models.User;
import com.repinsky.copywise.repositories.AccountRepository;
import com.repinsky.copywise.repositories.UserRepository;
import com.repinsky.copywise.utils.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountManagementService {
    private final AccountRepository accountRepository;
    private final AccountConverter accountConverter;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AccountPersistenceService accountPersistenceService;

    public List<AccountResponseDto> getAllAccounts() {
        String currentUserEmail = userService.getCurrentUserEmail();
        return accountRepository.findAllAccounts(currentUserEmail).orElseThrow(
                        () -> new AccountNotFoundException("Accounts for email '" + currentUserEmail + "' not found")
                )
                .stream()
                .map(accountConverter::entityToDto)
                .toList();
    }

    public AccountResponseDto getAccount(String accountNumber) {
        String currentUserEmail = userService.getCurrentUserEmail();
        Account account = accountRepository.findByAccountNumberAndUserEmail(accountNumber, currentUserEmail).orElseThrow(
                () -> new AccountNotFoundException("Account with number '" + accountNumber + "' for email '" + currentUserEmail + "' not found"));
        return accountConverter.entityToDto(account);
    }

    public String createAccount() {
        String currentUserEmail = userService.getCurrentUserEmail();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + currentUserEmail));

        Account account = new Account();
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setAccountBalance(BigDecimal.ZERO);
        account.setUser(user);
        account.setCreatedAt(LocalDateTime.now());

        accountPersistenceService.saveAccount(account);

        return "Account created successfully";
    }

    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
