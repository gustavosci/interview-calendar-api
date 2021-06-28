/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.validator;

import com.gsantos.calendar.interview.model.ddb.UserDDB;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(final String username, final UserType type, final Runnable action) {
        var user = getUserFromDDB(username);
        var isUserValid = user.stream().anyMatch(u -> u.getType().name().equals(type.name()));
        if (!isUserValid) action.run();
    }

    private Optional<UserDDB> getUserFromDDB(final String username) {
        return userRepository.getUserByUsername(username);
    }
}
