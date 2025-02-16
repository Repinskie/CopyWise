package com.repinsky.copywise.controllers;

import com.repinsky.copywise.dtos.JWTRequest;
import com.repinsky.copywise.dtos.JWTResponse;
import com.repinsky.copywise.dtos.RegisterUserRequest;
import com.repinsky.copywise.dtos.StringResponse;
import com.repinsky.copywise.exceptions.InputDataException;
import com.repinsky.copywise.jwt.JWTTokenUtil;
import com.repinsky.copywise.services.CustomUserDetailsService;
import com.repinsky.copywise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<JWTResponse> createAuthToken(@RequestBody JWTRequest authRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JWTResponse(token, customUserDetailsService.getRoles(authRequest.getEmail())));
    }

    @PostMapping("/register")
    public ResponseEntity<StringResponse> registerNewUser(@RequestBody RegisterUserRequest registerUserRequest) throws InputDataException {
        userService.createNewUser(registerUserRequest);
        return ResponseEntity.ok(new StringResponse("User with email '" + registerUserRequest.getEmail() + "' registered successfully"));
    }
}
