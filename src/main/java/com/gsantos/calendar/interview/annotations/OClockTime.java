/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.annotations;

import com.gsantos.calendar.interview.validator.OClockTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = OClockTimeValidator.class)
@Documented
public @interface OClockTime {
   String message() default "This time must start at the beginning of any hour.";
   Class<?>[] groups() default {};
   Class<? extends Payload>[] payload() default {};
}