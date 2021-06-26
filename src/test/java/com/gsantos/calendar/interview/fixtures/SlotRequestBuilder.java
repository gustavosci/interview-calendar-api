/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.model.request.AvailabilityRequest;

import java.time.LocalTime;

public final class SlotRequestBuilder {

    private SlotRequestBuilder() {
    }

    public static AvailabilityRequest.SlotRequest random() {
        var object = new AvailabilityRequest.SlotRequest();
        var time = LocalTime.of(LocalTime.now().getHour(), 0, 0);
        object.setStartTime(time);
        object.setEndTime(time.plusHours(1));
        return object;
    }
}
