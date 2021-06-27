/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;

import com.gsantos.calendar.interview.model.domain.DateAvailability;
import com.gsantos.calendar.interview.model.domain.Slot;
import com.gsantos.calendar.interview.model.response.CandidateAvailabilityResponse;
import com.gsantos.calendar.interview.model.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

class CandidateAvailabilityResponseMapperTest {

    private final CandidateAvailabilityResponseMapper mapper = new CandidateAvailabilityResponseMapper();

    @Test
    void shouldMapCandidateAvailabilityResponseSuccessfully() {
        // Given
        var candidate = randomAlphanumeric(10);
        var interviewer1 = randomAlphanumeric(10);
        var interviewer2 = randomAlphanumeric(10);

        var firstDateAvailabilityInterviewer1 = new DateAvailability(LocalDate.now(), List.of(
                new Slot(LocalTime.now(), LocalTime.now().plusHours(1)),
                new Slot(LocalTime.now().plusHours(5), LocalTime.now().plusHours(6))
            )
        );
        var secondDateAvailabilityInterviewer1 = new DateAvailability(LocalDate.now().plusDays(1), List.of(
                new Slot(LocalTime.now(), LocalTime.now().plusHours(1))
            )
        );

        var firstDateAvailabilityInterviewer2 = new DateAvailability(LocalDate.now().plusDays(10), List.of(
                new Slot(LocalTime.now().plusHours(7), LocalTime.now().plusHours(8))
            )
        );

        var matchesByInterviewer = Map.of(
                interviewer1, List.of(firstDateAvailabilityInterviewer1, secondDateAvailabilityInterviewer1),
                interviewer2, List.of(firstDateAvailabilityInterviewer2)
        );

        var expectedInterviewer1 = new CandidateAvailabilityResponse.InterviewerAvailabilityResponse(
                new UserResponse(interviewer1), mapDateSlotsToResponse(List.of(firstDateAvailabilityInterviewer1, secondDateAvailabilityInterviewer1))
        );
        var expectedInterviewer2 = new CandidateAvailabilityResponse.InterviewerAvailabilityResponse(
                new UserResponse(interviewer2), mapDateSlotsToResponse(List.of(firstDateAvailabilityInterviewer2))
        );

        var expected = new CandidateAvailabilityResponse(new UserResponse(candidate), List.of(expectedInterviewer1, expectedInterviewer2));

        // When
        var result = mapper.apply(candidate, matchesByInterviewer);

        // Then
        assertThat(result.getCandidate()).isEqualTo(expected.getCandidate());
        assertThat(result.getAvailableSlotsByInterviewer().toArray()).containsExactlyInAnyOrder(expected.getAvailableSlotsByInterviewer().toArray());
    }

    @Test
    void shouldMapCandidateAvailabilityToNullWhenCandidateIsNull() {
        // Given
        Map<String, List<DateAvailability>> matchesByInterviewer = Map.of();

        // When
        var result = mapper.apply(null, matchesByInterviewer);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldMapCandidateAvailabilityToNullWhenMatchesByInterviewerIsNull() {
        // Given
        var candidate = randomAlphanumeric(10);

        // When
        var result = mapper.apply(candidate, null);

        // Then
        assertThat(result).isNull();
    }

    private List<CandidateAvailabilityResponse.DateSlotsResponse> mapDateSlotsToResponse(final List<DateAvailability> dateSlots) {
        return dateSlots.stream().map(dateSlot -> {
            var slotsResponse = dateSlot.getSlots().stream()
                    .map(s -> new CandidateAvailabilityResponse.SlotResponse(s.getStartTime(), s.getEndTime()))
                    .collect(toList());
            return new CandidateAvailabilityResponse.DateSlotsResponse(dateSlot.getDate(), slotsResponse);
        }).collect(toList());
    }
}