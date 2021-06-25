/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.exception.UserNotFoundException;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SetAvailabilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetAvailabilityService.class);

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    protected SetAvailabilityService(CalendarRepository calendarRepository, UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
    }

    public void set(final String username, final AvailabilityRequest request) {
        LOGGER.info("Setting availability. Request: {}", request);
        validate(username);

        // TODO: VALIDATE TIME, MAP CALENDAR DDB AND SAVE

    }

    private void validate(final String username) {
        var user = userRepository.getUserByUsername(username);
        var isUserValid = user.stream().anyMatch(u -> u.getType().name().equals(getUserType().name()));
        if (!isUserValid) throw new UserNotFoundException();
    }

    protected abstract UserType getUserType();
}
