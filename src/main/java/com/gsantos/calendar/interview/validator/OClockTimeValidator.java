/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.validator;

import com.gsantos.calendar.interview.annotations.OClockTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class OClockTimeValidator implements ConstraintValidator<OClockTime, LocalTime> {

    @Override
    public boolean isValid(LocalTime time, ConstraintValidatorContext constraintValidatorContext) {
        if (time == null) {
            return true;
        }

        return time.getMinute() == 0 && time.getSecond() == 0;
    }
}