/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;

import com.gsantos.calendar.interview.model.domain.DateSlots;
import com.gsantos.calendar.interview.model.response.CandidateAvailableSlotsResponse;
import com.gsantos.calendar.interview.model.response.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

public class CandidateAvailableSlotsResponseMapper implements BiFunction<String, Map<String, List<DateSlots>>, CandidateAvailableSlotsResponse> {

    @Override
    public CandidateAvailableSlotsResponse apply(String username, Map<String, List<DateSlots>> matchesByInterviewer) {
        if (username == null || matchesByInterviewer == null) return null;

        var candidateResponse = new UserResponse(username);
        var interviewersAvailabilityResponse = matchesByInterviewer.entrySet().stream()
                .map(interviewerEntry -> {
                    var interviewerResponse = new UserResponse(interviewerEntry.getKey());
                    var dateSlotsResponse = mapDateSlotsToResponse(interviewerEntry.getValue());
                    return new CandidateAvailableSlotsResponse.InterviewerAvailabilityResponse(interviewerResponse, dateSlotsResponse);
                }).collect(toList());
        return new CandidateAvailableSlotsResponse(candidateResponse, interviewersAvailabilityResponse);
    }

    private List<CandidateAvailableSlotsResponse.DateSlotsResponse> mapDateSlotsToResponse(final List<DateSlots> dateSlots) {
        return dateSlots.stream().map(dateSlot -> {
            var slotsResponse = dateSlot.getSlots().stream()
                    .map(s -> new CandidateAvailableSlotsResponse.SlotResponse(s.getStartTime(), s.getEndTime()))
                    .collect(toList());
            return new CandidateAvailableSlotsResponse.DateSlotsResponse(dateSlot.getDate(), slotsResponse);
        }).collect(toList());
    }
}
