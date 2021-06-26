/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;

import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;

public class CalendarDDBMapper implements BiFunction<String, AvailabilityRequest.DateSlotsRequest, CalendarDDB> {

    // TODO: MOVE IT TO GENERIC/UTIL CLASS
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public CalendarDDB apply(String username, AvailabilityRequest.DateSlotsRequest request) {
        if (username == null || request == null ) return null;

        var calendar = new CalendarDDB();
        calendar.setUser(username);
        calendar.setDate(request.getDate().format(LOCAL_DATE_FORMATTER));
        calendar.setAvailableSlots(new SlotDDBMapper().apply(request.getAvailableSlots()));
        return calendar;
    }
}
