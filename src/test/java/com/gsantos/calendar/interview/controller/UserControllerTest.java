/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsantos.calendar.interview.exception.ConflictUserException;
import com.gsantos.calendar.interview.fixtures.RegisterUserRequestBuilder;
import com.gsantos.calendar.interview.service.RegisterUserService;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterUserService registerUserService;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        var request = RegisterUserRequestBuilder.random();

        // When
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated());

        // Then
        then(registerUserService).should().registerUser(any());
    }

    @Test
    void shouldReturnConflictStatusWhenUserAlreadyExists() throws Exception {
        // Given
        var request = RegisterUserRequestBuilder.random();
        doThrow(new ConflictUserException()).when(registerUserService).registerUser(any());

        // When
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isConflict());

        // Then
        then(registerUserService).should().registerUser(any());
    }

    @TestFactory
    Stream<DynamicTest> shouldValidateAllMandatoryFields() {
        return Stream.of(RegisterUserRequestBuilder.randomWithoutUsername(), RegisterUserRequestBuilder.randomWithoutName(), RegisterUserRequestBuilder.randomWithoutType())
                .map(request -> DynamicTest.dynamicTest(String.format("Should Validate: %s", request), () -> {
                    // When
                    mockMvc.perform(post("/v1/users")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(request)))
                            .andExpect(status().isBadRequest());

                    // Then
                    then(registerUserService).should(never()).registerUser(any());
                }));
    }
}