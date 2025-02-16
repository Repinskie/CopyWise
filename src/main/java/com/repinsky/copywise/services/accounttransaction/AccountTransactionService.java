package com.repinsky.copywise.services.accounttransaction;

import com.repinsky.copywise.exceptions.AccountNotFoundException;
import com.repinsky.copywise.exceptions.IllegalArgumentException;
import com.repinsky.copywise.exceptions.InsufficientFundsException;
import com.repinsky.copywise.models.Account;
import com.repinsky.copywise.repositories.AccountRepository;
import com.repinsky.copywise.services.AccountPersistenceService;
import com.repinsky.copywise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final AccountPersistenceService accountPersistenceService;

    @Transactional
    public String doTheDeposit(String accountNumber, BigDecimal amountToDeposit) {
        if (amountToDeposit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero, your deposit is " + amountToDeposit);
        }

        Account account = findAccountByAccountNumber(accountNumber);
        BigDecimal newBalance = account.getAccountBalance().add(amountToDeposit);

        account.setAccountBalance(newBalance);
        accountPersistenceService.saveAccount(account);

        return "Deposit '" + amountToDeposit + "' to account '" + accountNumber + "' was successfully processed, your total balance is " + newBalance;
    }

    @Transactional
    public String doTheWithdraw(String accountNumber, BigDecimal amountToWithdraw) {
        if (amountToWithdraw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero, your withdrawal is " + amountToWithdraw);
        }

        String currentUserEmail = userService.getCurrentUserEmail();
        Account account = findAccountByAccountNumberAndEmail(accountNumber, currentUserEmail);
        BigDecimal currentBalance = account.getAccountBalance();

        if (currentBalance.compareTo(amountToWithdraw) < 0) {
            throw new InsufficientFundsException("Not enough funds on account: " + accountNumber + ", account balance is " + currentBalance);
        }

        BigDecimal newBalance = currentBalance.subtract(amountToWithdraw);
        account.setAccountBalance(newBalance);
        accountPersistenceService.saveAccount(account);

        return "Withdrawal '" + amountToWithdraw + "' from account '" + accountNumber + "' was successfully processed, remaining balance is " + newBalance;
    }

    @Transactional
    public String toTheTransfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amountToTransfer) {
        if (amountToTransfer.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero, your transfer is " + amountToTransfer);
        }

        String currentUserEmail = userService.getCurrentUserEmail();
        Account senderAccount = findAccountByAccountNumberAndEmail(senderAccountNumber, currentUserEmail);
        Account receiverAccount = findAccountByAccountNumber(receiverAccountNumber);

        if (senderAccount.getAccountBalance().compareTo(amountToTransfer) < 0) {
            throw new InsufficientFundsException("Not enough funds on sender's account: " + senderAccountNumber + ", account balance is " + senderAccount.getAccountBalance());
        }

        BigDecimal senderNewBalance = senderAccount.getAccountBalance().subtract(amountToTransfer);
        BigDecimal receiverNewBalance = receiverAccount.getAccountBalance().add(amountToTransfer);

        senderAccount.setAccountBalance(senderNewBalance);
        receiverAccount.setAccountBalance(receiverNewBalance);

        accountPersistenceService.saveAccount(senderAccount);
        accountPersistenceService.saveAccount(receiverAccount);

        return "Transfer of '" + amountToTransfer + "' from account '" + senderAccountNumber + "' to account '" + receiverAccountNumber + "' was successfully processed";
    }

    private Account findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with number '" + accountNumber + "' not found"));
    }

    private Account findAccountByAccountNumberAndEmail(String accountNumber, String email) {
        return accountRepository.findByAccountNumberAndUserEmail(accountNumber, email)
                .orElseThrow(() -> new AccountNotFoundException("Account with number '" + accountNumber + "' for email '" + email + "' not found"));
    }
}
