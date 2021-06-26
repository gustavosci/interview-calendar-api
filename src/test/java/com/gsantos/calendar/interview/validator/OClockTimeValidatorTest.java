/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.validator;

import com.gsantos.calendar.interview.annotations.OClockTime;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class OClockTimeValidatorTest {

    private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldNotValidateNull() {
        var objectToValidate = new ObjectToValidate(null);
        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void shouldValidateTimeMinute() {
        var objectToValidate = new ObjectToValidate(LocalTime.of(15, 55, 0));
        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isNotEmpty();
    }

    @Test
    void shouldValidateTimeSecond() {
        var objectToValidate = new ObjectToValidate(LocalTime.of(15, 0, 55));
        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isNotEmpty();
    }

    @Test
    void shouldValidateCorrectTimeSuccessfully() {
        var objectToValidate = new ObjectToValidate(LocalTime.of(15, 0, 0));
        var constraintViolations = VALIDATOR.validate(objectToValidate);
        assertThat(constraintViolations).isEmpty();
    }

    private static class ObjectToValidate {
        @OClockTime
        private final LocalTime time;

        public ObjectToValidate(LocalTime time) {
            this.time = time;
        }
    }
}