/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.fixtures;

import com.gsantos.calendar.interview.model.request.AvailabilityRequest;

import java.util.List;

public final class AvailabilityRequestBuilder {

    private AvailabilityRequestBuilder() {
    }

    public static AvailabilityRequest random() {
        var object = new AvailabilityRequest();
        object.setAvailableSlotsByDate(List.of(DateSlotsRequestBuilder.random(), DateSlotsRequestBuilder.random()));
        return object;
    }

    public static AvailabilityRequest randomWithEmptyList() {
        var object = new AvailabilityRequest();
        object.setAvailableSlotsByDate(List.of());
        return object;
    }

    public static AvailabilityRequest randomWithDateAsNull() {
        var object = new AvailabilityRequest();
        var dateSlotsRequest = DateSlotsRequestBuilder.random();
        dateSlotsRequest.setDate(null);
        object.setAvailableSlotsByDate(List.of(dateSlotsRequest));
        return object;
    }

    public static AvailabilityRequest randomWithSlotsEmpty() {
        var object = new AvailabilityRequest();
        var dateSlotsRequest = DateSlotsRequestBuilder.random();
        dateSlotsRequest.setAvailableSlots(List.of());
        object.setAvailableSlotsByDate(List.of(dateSlotsRequest));
        return object;
    }
}
