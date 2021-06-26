/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.exception;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends IllegalStateException {

    private final HttpStatus status;

    protected HttpException(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public abstract String getDetailedMessage();
}
