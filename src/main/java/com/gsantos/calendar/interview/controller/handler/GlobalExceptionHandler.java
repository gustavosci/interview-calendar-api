/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.controller.handler;

import com.gsantos.calendar.interview.exception.UserNotFoundException;
import com.gsantos.calendar.interview.model.response.ErrorResponse;
import com.gsantos.calendar.interview.exception.ConflictUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        var fields = ex.getFieldErrors().stream()
                .map(f -> new ErrorResponse.FieldErrorResponse(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());
        var errorResponse = new ErrorResponse("Validation error", fields);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictUserException.class)
    public ResponseEntity handleConflictUserException(final ConflictUserException ex) {
        return new ResponseEntity<>(ex.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(final UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getStatus());
    }

    // Exception handler must always be the last one
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(final Exception ex) {
        var errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}