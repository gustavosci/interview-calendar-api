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
        object.setStartTime(LocalTime.now());
        object.setStartTime(LocalTime.now().plusHours(1));
        return object;
    }
}
