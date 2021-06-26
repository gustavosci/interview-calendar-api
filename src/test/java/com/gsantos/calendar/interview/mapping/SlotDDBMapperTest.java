/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;


import com.gsantos.calendar.interview.fixtures.SlotRequestBuilder;
import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SlotDDBMapperTest {

    private final SlotDDBMapper mapper = new SlotDDBMapper();

    @Test
    void shouldMapSlotRequestToSlotDDBSuccessfully() {
        // Given
        var slotsRequest = List.of(SlotRequestBuilder.random(), SlotRequestBuilder.random());
        var expected = slotsRequest.stream()
                .map(s -> new CalendarDDB.SlotDDB(s.getStartTime(), s.getEndTime()))
                .collect(Collectors.toList());

        // When
        var response = mapper.apply(slotsRequest);

        // Then
        assertThat(response)
                .hasSameSizeAs(slotsRequest)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNull() {
        // When
        var response = mapper.apply(null);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenEmpty() {
        // When
        var response = mapper.apply(List.of());

        // Then
        assertThat(response).isEmpty();
    }
}