package com.repinsky.copywise.services;

import com.repinsky.copywise.models.Account;
import com.repinsky.copywise.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountPersistenceService {
    private final AccountRepository accountRepository;

    @Transactional
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
