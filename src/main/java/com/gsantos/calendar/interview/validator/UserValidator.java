/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.validator;

import com.gsantos.calendar.interview.exception.ForbiddenUserException;
import com.gsantos.calendar.interview.exception.IncorrectUserTypeException;
import com.gsantos.calendar.interview.model.ddb.UserDDB;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidator.class);
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(final String username, final UserType type) {
        var user = getUserFromDDB(username);
        var isUserValid = user.stream().anyMatch(u -> u.getType().name().equals(type.name()));
        if (!isUserValid) throw new ForbiddenUserException();
    }

    // TODO: TEST
    public void validateUserType(final String username, final UserType type) {
        getUserFromDDB(username).ifPresent(u -> {
            if (!u.getType().name().equals(type.name())) {
                var message = String.format("It's expected for the user %s to be %s", username, type);
                LOGGER.warn(message);
                throw new IncorrectUserTypeException(message);
            }
        });
    }

    private Optional<UserDDB> getUserFromDDB(final String username) {
        return userRepository.getUserByUsername(username);
    }
}
