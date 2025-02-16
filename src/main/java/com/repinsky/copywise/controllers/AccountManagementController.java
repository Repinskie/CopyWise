package com.repinsky.copywise.controllers;

import com.repinsky.copywise.dtos.AccountResponseDto;
import com.repinsky.copywise.dtos.StringResponse;
import com.repinsky.copywise.services.accountmanagement.AccountManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountManagementController {

    private final AccountManagementService accountManagementService;

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAccounts() {
        return ResponseEntity.ok(accountManagementService.getAllAccounts());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountManagementService.getAccount(accountNumber));
    }

    @PostMapping
    public ResponseEntity<StringResponse> createAccount() {
        return ResponseEntity.ok(new StringResponse(accountManagementService.createAccount()));
    }
}
