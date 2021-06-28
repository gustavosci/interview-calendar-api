/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.exception;

import org.springframework.http.HttpStatus;

public abstract class BodyHttpException extends HttpException {

    protected BodyHttpException(HttpStatus status) {
        super(status);
    }

    public abstract String getDetailedMessage();
}
