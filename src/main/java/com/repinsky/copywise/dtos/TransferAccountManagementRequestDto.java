package com.repinsky.copywise.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferAccountManagementRequestDto {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
}
