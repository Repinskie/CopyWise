package com.repinsky.copywise.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountManagementRequestDto {
    private String accountNumber;
    private BigDecimal amount;
}
