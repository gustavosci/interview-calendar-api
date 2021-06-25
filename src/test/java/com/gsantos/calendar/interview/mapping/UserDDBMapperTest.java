/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.mapping;


import com.gsantos.calendar.interview.fixtures.RegisterUserRequestBuilder;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

class UserDDBMapperTest {

    private final UserDDBMapper mapper = new UserDDBMapper();

    @Test
    void shouldMapRegisterUserRequestToUserDDBSuccessfully() {
        // Given
        var registerUserRequest = RegisterUserRequestBuilder.random();

        // When
        var userDDB = mapper.apply(registerUserRequest);

        // Then
        assertThat(userDDB.getUsername()).isEqualTo(registerUserRequest.getUsername());
        assertThat(userDDB.getName()).isEqualTo(registerUserRequest.getName());
        assertThat(userDDB.getType().name()).isEqualTo(registerUserRequest.getType().name());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        // When
        var userDDB = mapper.apply(null);

        // Then
        assertThat(userDDB).isNull();
    }
}