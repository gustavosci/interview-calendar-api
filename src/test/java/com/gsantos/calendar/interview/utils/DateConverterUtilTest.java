/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DateConverterUtilTest {

    @Test
    void shouldConvertLocalDateToString() {
        // Given
        var date = LocalDate.of(1992, 10, 27);

        // When
        var result = DateConverterUtil.toString(date);

        // Then
        assertThat(result).isEqualTo("1992-10-27");
    }

    @Test
    void shouldNotConvertNullToString() {
        assertThat(DateConverterUtil.toString(null)).isNull();
    }

    @Test
    void shouldConvertStringToLocalDate() {
        // Given
        var dateStr = "1992-10-27";

        // When
        var result = DateConverterUtil.toLocalDate(dateStr);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(1992, 10, 27));
    }

    @Test
    void shouldNotConvertNullToLocalDate() {
        assertThat(DateConverterUtil.toLocalDate(null)).isNull();
    }
}
