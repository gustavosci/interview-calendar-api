/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import com.gsantos.calendar.interview.utils.DateConverterUtil;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class CalendarDDBMapper implements BiFunction<String, AvailabilityRequest.DateSlotsRequest, CalendarDDB> {

    @Override
    public CalendarDDB apply(String username, AvailabilityRequest.DateSlotsRequest request) {
        if (username == null || request == null ) return null;

        var calendar = new CalendarDDB();
        calendar.setUser(username);
        calendar.setDate(DateConverterUtil.toString(request.getDate()));
        calendar.setAvailableSlots(new SlotDDBMapper().apply(request.getAvailableSlots()));
        return calendar;
    }
}
