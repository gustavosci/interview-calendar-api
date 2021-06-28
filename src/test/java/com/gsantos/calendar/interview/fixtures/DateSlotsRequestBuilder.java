/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.model.request.AvailabilityRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public final class DateSlotsRequestBuilder {

    private DateSlotsRequestBuilder() {
    }

    public static AvailabilityRequest.DateSlotsRequest random() {
        var object = new AvailabilityRequest.DateSlotsRequest();
        object.setDate(LocalDate.now());
        object.setAvailableSlots(Set.of(
                SlotRequestBuilder.random(LocalTime.of(17, 0, 0)),
                SlotRequestBuilder.random(LocalTime.of(20, 0, 0))
        ));
        return object;
    }
}
