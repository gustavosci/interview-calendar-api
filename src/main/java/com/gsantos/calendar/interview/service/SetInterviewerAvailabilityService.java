/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.mapping.CalendarDDBMapper;
import com.gsantos.calendar.interview.mapping.SlotDDBMapper;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.validator.UserValidator;
import org.springframework.stereotype.Service;

@Service
public class SetInterviewerAvailabilityService extends SetAvailabilityService {

    public SetInterviewerAvailabilityService(CalendarRepository calendarRepository, UserValidator userValidator,
                                             SlotDDBMapper slotDDBMapper, CalendarDDBMapper calendarDDBMapper) {
        super(calendarRepository, userValidator, slotDDBMapper, calendarDDBMapper);
    }

    @Override
    protected UserType getUserType() {
        return UserType.INTERVIEWER;
    }
}
