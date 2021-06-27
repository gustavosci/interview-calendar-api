/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.annotations.validator;

import com.gsantos.calendar.interview.annotations.SlotPeriodTime;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SlotPeriodTimeValidatorTest {

    private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldNotValidateNull() {
        var objectToValidate = new ObjectToValidate(null);
        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void shouldValidateEndTimeEarlier() {
        var slot = new AvailabilityRequest.SlotRequest();
        slot.setStartTime(LocalTime.of(15, 0, 0));
        slot.setEndTime(LocalTime.of(14, 0, 0));
        var objectToValidate = new ObjectToValidate(slot);

        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isNotEmpty();
    }

    @Test
    void shouldValidateMaxPeriodHourReached() {
        var slot = new AvailabilityRequest.SlotRequest();
        slot.setStartTime(LocalTime.of(15, 0, 0));
        slot.setEndTime(LocalTime.of(17, 0, 0));
        var objectToValidate = new ObjectToValidate(slot);

        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isNotEmpty();
    }

    @Test
    void shouldValidateCorrectSlot() {
        var slot = new AvailabilityRequest.SlotRequest();
        slot.setStartTime(LocalTime.of(13, 0, 0));
        slot.setEndTime(LocalTime.of(14, 0, 0));
        var objectToValidate = new ObjectToValidate(slot);

        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isEmpty();
    }

    private static class ObjectToValidate {
        @SlotPeriodTime(maxPeriodHour = 1)
        private final AvailabilityRequest.SlotRequest slot;

        public ObjectToValidate(AvailabilityRequest.SlotRequest slot) {
            this.slot = slot;
        }
    }
}