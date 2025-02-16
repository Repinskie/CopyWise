package com.repinsky.copywise.converters;

import com.repinsky.copywise.dtos.AccountResponseDto;
import com.repinsky.copywise.models.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter {
    public AccountResponseDto entityToDto(Account account) {
        return new AccountResponseDto(
                account.getUser().getFirstName(),
                account.getUser().getLastName(),
                account.getUser().getEmail(),
                account.getAccountNumber(),
                account.getAccountBalance(),
                account.getCreatedAt()
        );
    }
}
