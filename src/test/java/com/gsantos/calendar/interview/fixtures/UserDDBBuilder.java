/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.domain.ddb.UserDDB;
import com.gsantos.calendar.interview.domain.request.RegisterUserRequest;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public final class UserDDBBuilder {

    private UserDDBBuilder() {
    }

    public static UserDDB random() {
        var object = new UserDDB();
        object.setUsername(randomAlphanumeric(10));
        object.setName(randomAlphanumeric(10));
        object.setType(UserDDB.UserType.values()[nextInt(0, RegisterUserRequest.UserType.values().length)]);
        return object;
    }
}
