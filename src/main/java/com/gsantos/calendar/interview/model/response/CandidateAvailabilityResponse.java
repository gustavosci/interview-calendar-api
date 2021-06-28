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
    private UserResponse candidate;
    private List<InterviewerAvailabilityResponse> availableSlotsByInterviewer;

    public CandidateAvailabilityResponse() {
    }

    public CandidateAvailabilityResponse(UserResponse candidate, List<InterviewerAvailabilityResponse> availableSlotsByInterviewer) {
        this.candidate = candidate;
        this.availableSlotsByInterviewer = availableSlotsByInterviewer;
    }

    public UserResponse getCandidate() {
        return candidate;
    }

    public void setCandidate(UserResponse candidate) {
        this.candidate = candidate;
    }

    public List<InterviewerAvailabilityResponse> getAvailableSlotsByInterviewer() {
        return availableSlotsByInterviewer;
    }

    public void setAvailableSlotsByInterviewer(List<InterviewerAvailabilityResponse> availableSlotsByInterviewer) {
        this.availableSlotsByInterviewer = availableSlotsByInterviewer;
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
        private UserResponse interviewer;
        private List<DateSlotsResponse> availableSlotsByDate;

        public InterviewerAvailabilityResponse() {
        }

        public InterviewerAvailabilityResponse(UserResponse interviewer, List<DateSlotsResponse> availableSlotsByDate) {
            this.interviewer = interviewer;
            this.availableSlotsByDate = availableSlotsByDate;
        }

        public UserResponse getInterviewer() {
            return interviewer;
        }

        public void setInterviewer(UserResponse interviewer) {
            this.interviewer = interviewer;
        }

        public List<DateSlotsResponse> getAvailableSlotsByDate() {
            return availableSlotsByDate;
        }

        public void setAvailableSlotsByDate(List<DateSlotsResponse> availableSlotsByDate) {
            this.availableSlotsByDate = availableSlotsByDate;
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
        private LocalDate date;
        private List<SlotResponse> slots;

        public DateSlotsResponse() {
        }

        public DateSlotsResponse(LocalDate date, List<SlotResponse> slots) {
            this.date = date;
            this.slots = slots;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public List<SlotResponse> getSlots() {
            return slots;
        }

        public void setSlots(List<SlotResponse> slots) {
            this.slots = slots;
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
        private LocalTime startTime;
        private LocalTime endTime;

        public SlotResponse() {
        }

        public SlotResponse(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
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
