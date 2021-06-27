/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.mapping.UserDDBMapper;
import com.gsantos.calendar.interview.model.ddb.UserDDB;
import com.gsantos.calendar.interview.exception.ConflictUserException;
import com.gsantos.calendar.interview.fixtures.RegisterUserRequestBuilder;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @InjectMocks
    private RegisterUserService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDDBMapper userDDBMapper;

    @Test
    void shouldCreateUserSuccessfully(){
        // Given
        var request = RegisterUserRequestBuilder.random();
        var expectedUserDDB = new UserDDB(request.getUsername(), request.getUsername(), UserDDB.UserType.valueOf(request.getType().name()));

        given(userRepository.getUserByUsername(request.getUsername())).willReturn(Optional.empty());
        given(userDDBMapper.apply(request)).willReturn(expectedUserDDB);

        // When
        service.registerUser(request);

        // Then
        then(userRepository).should().getUserByUsername(any());
        then(userDDBMapper).should().apply(any());
        then(userRepository).should().createUser(expectedUserDDB);
    }

    @Test
    void shouldThrowConflictExceptionWhenUserAlreadyExists(){
        // Given
        var request = RegisterUserRequestBuilder.random();

        given(userRepository.getUserByUsername(request.getUsername())).willReturn(Optional.of(new UserDDB()));

        // When
        Assertions.assertThrows(ConflictUserException.class, () -> service.registerUser(request));

        // Then
        then(userRepository).should().getUserByUsername(any());
        then(userDDBMapper).should(never()).apply(any());
        then(userRepository).should(never()).createUser(any());
    }
}