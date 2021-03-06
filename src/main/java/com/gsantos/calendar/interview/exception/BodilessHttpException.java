/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.exception;

import org.springframework.http.HttpStatus;

public abstract class BodilessHttpException extends HttpException {

    protected BodilessHttpException(HttpStatus status) {
        super(status);
    }
}
