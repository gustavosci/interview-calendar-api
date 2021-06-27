/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.annotations.validator;

import com.gsantos.calendar.interview.annotations.SlotPeriodTime;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SlotPeriodTimeValidator implements ConstraintValidator<SlotPeriodTime, AvailabilityRequest.SlotRequest> {

    private int maxPeriodHour;

    @Override
    public void initialize(SlotPeriodTime slotPeriodTime) {
        maxPeriodHour = slotPeriodTime.maxPeriodHour();
    }

    @Override
    public boolean isValid(AvailabilityRequest.SlotRequest slot, ConstraintValidatorContext constraintValidatorContext) {
        if (slot == null) {
            return true;
        }

        return slot.getStartTime().isBefore(slot.getEndTime()) && !slot.getStartTime().plusHours(maxPeriodHour).isBefore(slot.getEndTime());
    }
}