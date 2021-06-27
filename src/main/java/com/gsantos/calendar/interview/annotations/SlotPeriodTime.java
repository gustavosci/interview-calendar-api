/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.annotations;

import com.gsantos.calendar.interview.annotations.validator.SlotPeriodTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = SlotPeriodTimeValidator.class)
@Documented
public @interface SlotPeriodTime {
   String message() default "The slot period is not valid.";
   Class<?>[] groups() default {};
   Class<? extends Payload>[] payload() default {};
   int maxPeriodHour();
}