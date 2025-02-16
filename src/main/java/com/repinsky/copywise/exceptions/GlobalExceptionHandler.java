package com.repinsky.copywise.exceptions;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.repinsky.copywise.constants.InfoMessage.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new AppError(RESOURCE_NOT_FOUND_CODE.getValue(), e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleBadCredentialsException(BadCredentialsException e) {
        return new ResponseEntity<>(
                new AppError(CHECK_USERNAME_PASSWORD_ERROR_CODE.getValue(), INVALID_EMAIL_OR_PASSWORD.getValue()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new AppError(USER_NOT_FOUND_CODE.getValue(), e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleTaskNotFoundException(AccountNotFoundException e) {
        return new ResponseEntity<>(new AppError(ACCOUNT_NOT_FOUND_CODE.getValue(), e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleBadRequestException(BadRequestException e){
        return new ResponseEntity<>(new AppError(BAD_REQUEST_ERROR_CODE.getValue(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleInputDataException(InputDataException e){
        return new ResponseEntity<>(new AppError(INPUT_DATA_ERROR_CODE.getValue(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleInsufficientFundsException(InsufficientFundsException e){
        return new ResponseEntity<>(new AppError(INSUFFICIENT_FUNDS_ERROR_CODE.getValue(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleIllegalArgumentException(IllegalArgumentException e){
        return new ResponseEntity<>(new AppError(ILLEGAL_ARGUMENT_ERROR_CODE.getValue(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
