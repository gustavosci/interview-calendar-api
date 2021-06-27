/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateAvailabilityResponse that = (CandidateAvailabilityResponse) o;
        return Objects.equals(candidate, that.candidate) &&
                Objects.equals(availableSlotsByInterviewer, that.availableSlotsByInterviewer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidate, availableSlotsByInterviewer);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InterviewerAvailabilityResponse that = (InterviewerAvailabilityResponse) o;
            return Objects.equals(interviewer, that.interviewer) &&
                    Objects.equals(availableSlotsByDate, that.availableSlotsByDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(interviewer, availableSlotsByDate);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DateSlotsResponse that = (DateSlotsResponse) o;
            return Objects.equals(date, that.date) &&
                    Objects.equals(slots, that.slots);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, slots);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SlotResponse that = (SlotResponse) o;
            return Objects.equals(startTime, that.startTime) &&
                    Objects.equals(endTime, that.endTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startTime, endTime);
        }
    }
}
