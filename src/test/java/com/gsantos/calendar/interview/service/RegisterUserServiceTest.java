/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.domain.ddb.UserDDB;
import com.gsantos.calendar.interview.exception.ConflictUserException;
import com.gsantos.calendar.interview.fixtures.RegisterUserRequestBuilder;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Captor
    private ArgumentCaptor<UserDDB> userDDBCaptor;

    @Test
    void shouldCreateUserSuccessfully(){
        // Given
        var request = RegisterUserRequestBuilder.random();

        given(userRepository.getUserByUsername(request.getUsername())).willReturn(Optional.empty());

        // When
        service.registerUser(request);

        // Then
        then(userRepository).should().getUserByUsername(any());
        then(userRepository).should().createUser(userDDBCaptor.capture());

        assertThat(userDDBCaptor.getValue().getUsername()).isEqualTo(request.getUsername());
        assertThat(userDDBCaptor.getValue().getName()).isEqualTo(request.getName());
        assertThat(userDDBCaptor.getValue().getType().name()).isEqualTo(request.getType().name());
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
        then(userRepository).should(never()).createUser(any());
    }
}