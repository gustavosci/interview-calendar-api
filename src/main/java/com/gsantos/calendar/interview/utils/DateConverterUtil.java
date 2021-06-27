/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateConverterUtil {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateConverterUtil() { }

    public static String toString(final LocalDate date) {
        if (date == null) return null;
        return date.format(LOCAL_DATE_FORMATTER);
    }

    public static LocalDate toLocalDate(final String date) {
        if (date == null) return null;
        return LocalDate.parse(date, LOCAL_DATE_FORMATTER);
    }
}
