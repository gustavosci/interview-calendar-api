/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.domain.request.RegisterUserRequest;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public final class RegisterUserRequestBuilder {

    private RegisterUserRequestBuilder() {
    }

    public static RegisterUserRequest random() {
        var object = new RegisterUserRequest();
        object.setUsername(randomAlphanumeric(10));
        object.setName(randomAlphanumeric(10));
        object.setType(RegisterUserRequest.UserType.values()[nextInt(0, RegisterUserRequest.UserType.values().length)]);
        return object;
    }

    public static RegisterUserRequest randomWithoutUsername() {
        var object = new RegisterUserRequest();
        object.setName(randomAlphanumeric(10));
        object.setType(RegisterUserRequest.UserType.values()[nextInt(0, RegisterUserRequest.UserType.values().length)]);
        return object;
    }

    public static RegisterUserRequest randomWithoutName() {
        var object = new RegisterUserRequest();
        object.setUsername(randomAlphanumeric(10));
        object.setType(RegisterUserRequest.UserType.values()[nextInt(0, RegisterUserRequest.UserType.values().length)]);
        return object;
    }

    public static RegisterUserRequest randomWithoutType() {
        var object = new RegisterUserRequest();
        object.setUsername(randomAlphanumeric(10));
        object.setName(randomAlphanumeric(10));
        return object;
    }
}
