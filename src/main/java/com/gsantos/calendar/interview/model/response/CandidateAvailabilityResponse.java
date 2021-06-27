/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateAvailabilityResponse {
    private final UserResponse candidate;
    private final List<InterviewerAvailabilityResponse> availableSlotsByInterviewer;

    public CandidateAvailabilityResponse(UserResponse candidate, List<InterviewerAvailabilityResponse> availableSlotsByInterviewer) {
        this.candidate = candidate;
        this.availableSlotsByInterviewer = availableSlotsByInterviewer;
    }

    public UserResponse getCandidate() {
        return candidate;
    }

    public List<InterviewerAvailabilityResponse> getAvailableSlotsByInterviewer() {
        return availableSlotsByInterviewer;
    }

    public static class InterviewerAvailabilityResponse {
        private final UserResponse interviewer;
        private final List<DateSlotsResponse> availableSlotsByDate;

        public InterviewerAvailabilityResponse(UserResponse interviewer, List<DateSlotsResponse> availableSlotsByDate) {
            this.interviewer = interviewer;
            this.availableSlotsByDate = availableSlotsByDate;
        }

        public UserResponse getInterviewer() {
            return interviewer;
        }

        public List<DateSlotsResponse> getAvailableSlotsByDate() {
            return availableSlotsByDate;
        }
    }

    public static class DateSlotsResponse {
        private final LocalDate date;
        private final List<SlotResponse> slots;

        public DateSlotsResponse(LocalDate date, List<SlotResponse> slots) {
            this.date = date;
            this.slots = slots;
        }

        public LocalDate getDate() {
            return date;
        }

        public List<SlotResponse> getSlots() {
            return slots;
        }
    }

    public static class SlotResponse {
        private final LocalTime startTime;
        private final LocalTime endTime;

        public SlotResponse(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }
    }
}
