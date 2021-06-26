/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public final class CalendarDDBBuilder {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private CalendarDDBBuilder() {
    }

    public static CalendarDDB random() {
        var object = new CalendarDDB();
        object.setDate(LocalDate.now().format(LOCAL_DATE_FORMATTER));
        object.setUser(randomAlphanumeric(10));
        object.setAvailableSlots(List.of(SlotDDBBuilder.random(), SlotDDBBuilder.random()));
        return object;
    }
}
