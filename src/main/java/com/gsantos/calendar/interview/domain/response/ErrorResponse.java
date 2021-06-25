/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String message;
    private String field;

    public ErrorResponse(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "message='" + message + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
