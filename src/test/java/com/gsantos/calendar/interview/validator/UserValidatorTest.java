/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.validator;

import com.gsantos.calendar.interview.exception.ForbiddenUserException;
import com.gsantos.calendar.interview.fixtures.UserDDBBuilder;
import com.gsantos.calendar.interview.model.ddb.UserDDB;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @InjectMocks
    private UserValidator validator;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldValidateUserSuccessfully() {
        // Given
        var userDDB = UserDDBBuilder.random();
        var username = randomAlphanumeric(10);

        given(userRepository.getUserByUsername(username)).willReturn(Optional.of(userDDB));

        // When
        validator.validate(username, UserType.valueOf(userDDB.getType().name()));

        // Then
        then(userRepository).should().getUserByUsername(any());
    }

    @Test
    void shouldThrowErrorWhenUserDoesNotExist(){
        // Given
        var username = randomAlphanumeric(10);

        given(userRepository.getUserByUsername(username)).willReturn(Optional.empty());

        // When
        Assertions.assertThrows(ForbiddenUserException.class, () -> validator.validate(username, UserType.CANDIDATE));

        // Then
        then(userRepository).should().getUserByUsername(any());
    }

    @Test
    void shouldThrowErrorWhenUserIsNotExpectedType(){
        // Given
        var userDDB = UserDDBBuilder.random();
        var username = randomAlphanumeric(10);
        var userType = userDDB.getType() == UserDDB.UserType.CANDIDATE ? UserType.INTERVIEWER : UserType.CANDIDATE;

        given(userRepository.getUserByUsername(username)).willReturn(Optional.of(userDDB));

        // When
        Assertions.assertThrows(ForbiddenUserException.class, () -> validator.validate(username, userType));

        // Then
        then(userRepository).should().getUserByUsername(any());
    }
}