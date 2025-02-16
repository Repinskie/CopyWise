package com.repinsky.copywise.controllers;

import com.repinsky.copywise.dtos.AccountManagementRequestDto;
import com.repinsky.copywise.dtos.StringResponse;
import com.repinsky.copywise.dtos.TransferAccountManagementRequestDto;
import com.repinsky.copywise.services.accounttransaction.AccountTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountTransactionController {

    private final AccountTransactionService accountTransactionService;

    @PostMapping("/deposit")
    public ResponseEntity<StringResponse> depositFunds(@RequestBody AccountManagementRequestDto requestDto) {
        return ResponseEntity.ok(new StringResponse(accountTransactionService.doTheDeposit(requestDto.getAccountNumber(), requestDto.getAmount())));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<StringResponse> withdrawFunds(@RequestBody AccountManagementRequestDto requestDto) {
        return ResponseEntity.ok(new StringResponse(accountTransactionService.doTheWithdraw(requestDto.getAccountNumber(), requestDto.getAmount())));
    }

    @PostMapping("/transfer")
    public ResponseEntity<StringResponse> transferFunds(@RequestBody TransferAccountManagementRequestDto requestDto) {
        return ResponseEntity.ok(new StringResponse(accountTransactionService.toTheTransfer(requestDto.getSenderAccountNumber(), requestDto.getReceiverAccountNumber(), requestDto.getAmount())));
    }
}
