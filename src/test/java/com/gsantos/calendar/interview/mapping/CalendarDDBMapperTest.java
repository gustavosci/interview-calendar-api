/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;


import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarDDBMapperTest {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final CalendarDDBMapper mapper = new CalendarDDBMapper();

    @Test
    void shouldMapRequestToCalendarDDBSuccessfully() {
        // Given
        var dateSlotsRequest = new AvailabilityRequest.DateSlotsRequest(LocalDate.now().plusDays(1), List.of());
        var username = "anyuser";

        // When
        var response = mapper.apply(username, dateSlotsRequest);

        // Then
        assertThat(response.getDate()).isEqualTo(dateSlotsRequest.getDate().format(LOCAL_DATE_FORMATTER));
        assertThat(response.getUser()).isEqualTo(username);
        assertThat(response.getAvailableSlots()).isEmpty();
    }

    @Test
    void shouldReturnNullWhenUsernameIsNull() {
        // When
        var response = mapper.apply(null, new AvailabilityRequest.DateSlotsRequest());

        // Then
        assertThat(response).isNull();
    }

    @Test
    void shouldReturnNullWhenDateSlotsRequestIsNull() {
        // When
        var response = mapper.apply("anyuser", null);

        // Then
        assertThat(response).isNull();
    }
}