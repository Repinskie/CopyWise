package com.repinsky.copywise.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponse {
    private String token;
    private List<String> roles;
}
