/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.validator.UserValidator;
import org.springframework.stereotype.Service;

@Service
public class SetCandidateAvailabilityService extends SetAvailabilityService {

    public SetCandidateAvailabilityService(CalendarRepository calendarRepository, UserValidator userValidator) {
        super(calendarRepository, userValidator);
    }

    @Override
    protected UserType getUserType() {
        return UserType.CANDIDATE;
    }
}
