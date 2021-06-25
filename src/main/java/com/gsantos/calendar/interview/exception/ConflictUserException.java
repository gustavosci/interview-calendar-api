/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.exception;

import org.springframework.http.HttpStatus;

public class ConflictUserException extends IllegalStateException {

    private final HttpStatus status;

    public ConflictUserException() {
        this.status = HttpStatus.CONFLICT;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
