/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;

import java.time.LocalTime;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public final class SlotDDBBuilder {

    private SlotDDBBuilder() {
    }

    public static CalendarDDB.SlotDDB random() {
        var object = new CalendarDDB.SlotDDB();
        object.setStartTime(LocalTime.now().plusMinutes(nextInt(1, 59)));
        object.setStartTime(LocalTime.now().plusHours(nextInt(1, 5)));
        return object;
    }
}
