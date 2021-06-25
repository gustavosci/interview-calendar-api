/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;

import com.gsantos.calendar.interview.domain.ddb.UserDDB;
import com.gsantos.calendar.interview.domain.request.RegisterUserRequest;

import java.util.function.Function;

public class UserDDBMapper implements Function<RegisterUserRequest, UserDDB> {

    @Override
    public UserDDB apply(RegisterUserRequest request) {
        if (request == null) return null;

        return new UserDDB(
                request.getUsername(),
                request.getName(),
                UserDDB.UserType.valueOf(request.getType().name())
        );
    }
}
