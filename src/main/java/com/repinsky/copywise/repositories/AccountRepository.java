package com.repinsky.copywise.repositories;

import com.repinsky.copywise.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a where a.user.email = :email")
    Optional<List<Account>> findAllAccounts(String email);

    Optional<Account> findByAccountNumberAndUserEmail(String accountNumber, String currentUserEmail);

    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);
}
