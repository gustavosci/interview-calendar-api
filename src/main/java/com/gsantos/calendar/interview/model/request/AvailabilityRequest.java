/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.model.request;

import com.gsantos.calendar.interview.annotations.OClockTime;
import com.gsantos.calendar.interview.annotations.SlotPeriodTime;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AvailabilityRequest {

    @NotEmpty
    private List<@Valid DateSlotsRequest> availableSlotsByDate;

    public AvailabilityRequest() {
    }

    public AvailabilityRequest(@NotEmpty List<@Valid DateSlotsRequest> availableSlotsByDate) {
        this.availableSlotsByDate = availableSlotsByDate;
    }

    public List<DateSlotsRequest> getAvailableSlotsByDate() {
        return availableSlotsByDate;
    }

    public void setAvailableSlotsByDate(List<DateSlotsRequest> availableSlotsByDate) {
        this.availableSlotsByDate = availableSlotsByDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilityRequest that = (AvailabilityRequest) o;
        return Objects.equals(availableSlotsByDate, that.availableSlotsByDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableSlotsByDate);
    }

    @Override
    public String toString() {
        return "AvailabilitySlotsRequest{" +
                "availableSlotsByDate=" + availableSlotsByDate +
                '}';
    }

    public static class DateSlotsRequest {
        @NotNull
        private LocalDate date;

        @NotEmpty
        private Set<@Valid @SlotPeriodTime(maxPeriodHour = 1) SlotRequest> availableSlots;

        public DateSlotsRequest() {
        }

        public DateSlotsRequest(@NotNull LocalDate date, @NotEmpty Set<@Valid @SlotPeriodTime(maxPeriodHour = 1) SlotRequest> availableSlots) {
            this.date = date;
            this.availableSlots = availableSlots;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Set<SlotRequest> getAvailableSlots() {
            return availableSlots;
        }

        public void setAvailableSlots(Set<SlotRequest> availableSlots) {
            this.availableSlots = availableSlots;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DateSlotsRequest that = (DateSlotsRequest) o;
            return Objects.equals(date, that.date) &&
                    Objects.equals(availableSlots, that.availableSlots);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, availableSlots);
        }

        @Override
        public String toString() {
            return "DateSlotsRequest{" +
                    "date=" + date +
                    ", availableSlots=" + availableSlots +
                    '}';
        }
    }

    public static class SlotRequest {
        @NotNull
        @OClockTime
        private LocalTime startTime;

        @NotNull
        @OClockTime
        private LocalTime endTime;

        public SlotRequest() {
        }

        public SlotRequest(@NotNull LocalTime startTime, @NotNull LocalTime endTime) {
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
            SlotRequest that = (SlotRequest) o;
            return Objects.equals(startTime, that.startTime) &&
                    Objects.equals(endTime, that.endTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startTime, endTime);
        }

        @Override
        public String toString() {
            return "SlotRequest{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    '}';
        }
    }
}
