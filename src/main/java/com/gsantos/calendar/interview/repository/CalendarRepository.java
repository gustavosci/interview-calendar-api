/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.repository;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.utils.DateConverterUtil;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class CalendarRepository {

    private final DynamoDbTable<CalendarDDB> calendarDDBTable;

    public CalendarRepository(DynamoDbTable<CalendarDDB> calendarDDBTable) {
        this.calendarDDBTable = calendarDDBTable;
    }

    public void save(final CalendarDDB calendar) {
        calendarDDBTable.putItem(calendar);
    }

    public Optional<CalendarDDB> getCalendarByUserAndDate(final String username, final LocalDate localDate) {
        var key = Key.builder()
                .partitionValue(username)
                .sortValue(DateConverterUtil.toString(localDate))
                .build();
        return Optional.ofNullable(calendarDDBTable.getItem(key));
    }
}
