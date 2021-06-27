/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.utils.DateConverterUtil;

import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public final class CalendarDDBBuilder {

    private CalendarDDBBuilder() {
    }

    public static CalendarDDB random() {
        var object = new CalendarDDB();
        object.setDate(DateConverterUtil.toString(LocalDate.now()));
        object.setUser(randomAlphanumeric(10));
        object.setAvailableSlots(List.of(SlotDDBBuilder.random(), SlotDDBBuilder.random()));
        return object;
    }
}
