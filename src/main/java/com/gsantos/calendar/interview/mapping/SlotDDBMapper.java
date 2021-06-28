/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SlotDDBMapper implements Function<Set<AvailabilityRequest.SlotRequest>, List<CalendarDDB.SlotDDB>> {

    @Override
    public List<CalendarDDB.SlotDDB> apply(Set<AvailabilityRequest.SlotRequest> slotRequests) {
        if (slotRequests == null || slotRequests.isEmpty()) {
            return List.of();
        }

        return slotRequests.stream()
                .map(s -> new CalendarDDB.SlotDDB(s.getStartTime(), s.getEndTime()))
                .collect(Collectors.toList());
    }
}
