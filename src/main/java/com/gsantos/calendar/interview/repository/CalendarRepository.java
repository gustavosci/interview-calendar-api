/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.repository;

import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Repository
public class CalendarRepository {

    private final DynamoDbTable<CalendarDDB> calendarDDBTable;

    public CalendarRepository(DynamoDbTable<CalendarDDB> calendarDDBTable) {
        this.calendarDDBTable = calendarDDBTable;
    }
}
