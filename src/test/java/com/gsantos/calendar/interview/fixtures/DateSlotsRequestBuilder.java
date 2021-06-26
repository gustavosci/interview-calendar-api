/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.model.request.AvailabilityRequest;

import java.time.LocalDate;
import java.util.List;

public final class DateSlotsRequestBuilder {

    private DateSlotsRequestBuilder() {
    }

    public static AvailabilityRequest.DateSlotsRequest random() {
        var object = new AvailabilityRequest.DateSlotsRequest();
        object.setDate(LocalDate.now());
        object.setAvailableSlots(List.of(SlotRequestBuilder.random(), SlotRequestBuilder.random()));
        return object;
    }
}
