/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.exception;

import org.springframework.http.HttpStatus;

public class UnexpectedUserTypeException extends BodyHttpException {

    private final String message;

    public UnexpectedUserTypeException(final String message) {
        super(HttpStatus.BAD_REQUEST);
        this.message = message;
    }

    @Override
    public String getDetailedMessage() {
        return message;
    }
}
