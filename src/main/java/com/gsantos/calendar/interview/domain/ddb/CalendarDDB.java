/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.domain.ddb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalTime;
import java.util.List;

@DynamoDbBean
public class CalendarDDB {
    private String id;
    private List<SlotDDB> availableSlots;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    }
}
