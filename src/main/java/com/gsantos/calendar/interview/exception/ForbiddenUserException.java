/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenUserException extends BodilessHttpException {

    public ForbiddenUserException() {
        super(HttpStatus.FORBIDDEN);
    }
}
