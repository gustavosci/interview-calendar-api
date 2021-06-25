/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.exception;

import org.springframework.http.HttpStatus;

// TODO: CREATE ABSTRACT EXCEPTION
public class UserNotFoundException extends IllegalStateException {

    private final HttpStatus status;

    public UserNotFoundException() {
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
