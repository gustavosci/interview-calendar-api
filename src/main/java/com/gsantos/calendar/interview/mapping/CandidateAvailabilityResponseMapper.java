/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;

import com.gsantos.calendar.interview.model.domain.DateAvailability;
import com.gsantos.calendar.interview.model.response.CandidateAvailabilityResponse;
import com.gsantos.calendar.interview.model.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

@Component
public class CandidateAvailabilityResponseMapper implements BiFunction<String, Map<String, List<DateAvailability>>, CandidateAvailabilityResponse> {

    @Override
    public CandidateAvailabilityResponse apply(String username, Map<String, List<DateAvailability>> matchesByInterviewer) {
        if (username == null || matchesByInterviewer == null) return null;

        var candidateResponse = new UserResponse(username);
        var interviewersAvailabilityResponse = matchesByInterviewer.entrySet().stream()
                .map(interviewerEntry -> {
                    var interviewerResponse = new UserResponse(interviewerEntry.getKey());
                    var dateSlotsResponse = mapDateSlotsToResponse(interviewerEntry.getValue());
                    return new CandidateAvailabilityResponse.InterviewerAvailabilityResponse(interviewerResponse, dateSlotsResponse);
                }).collect(toList());
        return new CandidateAvailabilityResponse(candidateResponse, interviewersAvailabilityResponse);
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
