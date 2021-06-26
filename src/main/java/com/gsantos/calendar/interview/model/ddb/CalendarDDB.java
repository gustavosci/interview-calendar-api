/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.model.ddb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@DynamoDbBean
public class CalendarDDB {
    private String user;
    private String date;
    private List<SlotDDB> availableSlots;

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "calendar-index")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = "calendar-index")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<SlotDDB> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<SlotDDB> availableSlots) {
        this.availableSlots = availableSlots;
    }

    @DynamoDbBean
    public static class SlotDDB {
        private LocalTime startTime;
        private LocalTime endTime;

        public SlotDDB() {
        }

        public SlotDDB(LocalTime startTime, LocalTime endTime) {
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
            SlotDDB slotDDB = (SlotDDB) o;
            return Objects.equals(startTime, slotDDB.startTime) &&
                    Objects.equals(endTime, slotDDB.endTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startTime, endTime);
        }
    }
}
