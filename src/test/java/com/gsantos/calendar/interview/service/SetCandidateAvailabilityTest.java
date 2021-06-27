/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SetCandidateAvailabilityTest {

    @InjectMocks
    private SetCandidateAvailabilityService service;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private UserValidator userValidator;

    @Test
    void shouldGetUserTypeSuccessfully(){
        // When
        var userType = service.getUserType();

        // Then
        assertThat(userType).isEqualTo(UserType.CANDIDATE);
    }

    @Test
    void shouldExtendsSetAvailabilityService() {
        assertThat(SetAvailabilityService.class.isAssignableFrom(SetCandidateAvailabilityService.class)).isTrue();
    }
}