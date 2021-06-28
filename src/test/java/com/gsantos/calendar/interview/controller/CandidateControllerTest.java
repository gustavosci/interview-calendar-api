/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsantos.calendar.interview.exception.ForbiddenUserException;
import com.gsantos.calendar.interview.exception.SlotOverlappedException;
import com.gsantos.calendar.interview.fixtures.AvailabilityRequestBuilder;
import com.gsantos.calendar.interview.model.response.CandidateAvailabilityResponse;
import com.gsantos.calendar.interview.model.response.UserResponse;
import com.gsantos.calendar.interview.service.GetCandidateAvailabilityService;
import com.gsantos.calendar.interview.service.SetCandidateAvailabilityService;
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

import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CandidateController.class)
class CandidateControllerTest {

    private static final String CANDIDATE_RESOURCE = "/v1/candidates";
    private static final String PATH_AVAILABILITY = CANDIDATE_RESOURCE + "/availability";
    private static final String USERNAME_HEADER = "username";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SetCandidateAvailabilityService setCandidateAvailabilityService;

    @MockBean
    private GetCandidateAvailabilityService getCandidateAvailabilityService;

    // POST /availability
    @Test
    void shouldSetInterviewerAvailabilitySuccessfully_POSTAvailability() throws Exception {
        // Given
        var request = AvailabilityRequestBuilder.random();
        var username = randomAlphanumeric(10);

        // When
        mockMvc.perform(post(PATH_AVAILABILITY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(USERNAME_HEADER, username)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        // Then
        then(setCandidateAvailabilityService).should().set(username, request);
    }

    @Test
    void shouldReturnBadRequestWhenSlotIsOverlapped_POSTAvailability() throws Exception {
        // Given
        var request = AvailabilityRequestBuilder.random();
        doThrow(new SlotOverlappedException("any")).when(setCandidateAvailabilityService).set(any(), any());

        // When
        mockMvc.perform(post(PATH_AVAILABILITY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(USERNAME_HEADER, "Any")
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());

        // Then
        then(setCandidateAvailabilityService).should().set(any(), any());
    }

    @Test
    void shouldReturnForbiddenWhenUserIsForbidden_POSTAvailability() throws Exception {
        // Given
        var request = AvailabilityRequestBuilder.random();
        doThrow(new ForbiddenUserException()).when(setCandidateAvailabilityService).set(any(), any());

        // When
        mockMvc.perform(post(PATH_AVAILABILITY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(USERNAME_HEADER, "Any")
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());

        // Then
        then(setCandidateAvailabilityService).should().set(any(), any());
    }

    @Test
    void shouldReturnInternalServerErrorWhenAnyOtherErrorHappens_POSTAvailability() throws Exception {
        // Given
        var request = AvailabilityRequestBuilder.random();
        doThrow(new RuntimeException("kaboom")).when(setCandidateAvailabilityService).set(any(), any());

        // When
        mockMvc.perform(post(PATH_AVAILABILITY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(USERNAME_HEADER, "Any")
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isInternalServerError());

        // Then
        then(setCandidateAvailabilityService).should().set(any(), any());
    }

    @Test
    void shouldReturnBadRequestWhenMandatoryHeaderIsMissing_POSTAvailability() throws Exception {
        // Given
        var request = AvailabilityRequestBuilder.random();

        // When
        mockMvc.perform(post(PATH_AVAILABILITY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());

        // Then
        then(setCandidateAvailabilityService).should(never()).set(any(), any());
    }

    @TestFactory
    Stream<DynamicTest> shouldValidateAllMandatoryFields_POSTAvailability() {
        return Stream.of(
                AvailabilityRequestBuilder.randomWithEmptyList(),
                AvailabilityRequestBuilder.randomWithDateAsNull(),
                AvailabilityRequestBuilder.randomWithSlotsEmpty()
        ).map(request -> DynamicTest.dynamicTest(String.format("Should Validate: %s", request), () -> {
            // When
            mockMvc.perform(post(PATH_AVAILABILITY)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(USERNAME_HEADER, "Any")
                    .content(objectMapper.writeValueAsBytes(request)))
                    .andExpect(status().isBadRequest());
            // Then
            then(setCandidateAvailabilityService).should(never()).set(any(), any());
        }));
    }

    // GET /availability

    @Test
    void shouldReturnCandidateAvailabilitySuccessfully_GETAvailability() throws Exception {
        // Given
        var candidate = randomAlphanumeric(10);
        var interviewer1 = randomAlphanumeric(10);
        var interviewer2 = randomAlphanumeric(10);
        var daysInFuture = nextInt(10, 20);

        var expectedResult = new CandidateAvailabilityResponse(
                new UserResponse(candidate),
                List.of(
                        new CandidateAvailabilityResponse.InterviewerAvailabilityResponse(new UserResponse(interviewer1), List.of()),
                        new CandidateAvailabilityResponse.InterviewerAvailabilityResponse(new UserResponse(interviewer2), List.of())
                )
        );
        given(getCandidateAvailabilityService.get(candidate, List.of(interviewer1, interviewer2), daysInFuture)).willReturn(expectedResult);

        // When
        var response = mockMvc.perform(get(PATH_AVAILABILITY)
                .header(USERNAME_HEADER, candidate)
                .param("interviewers", String.format("%s,%s", interviewer1, interviewer2))
                .param("daysInFuture", String.valueOf(daysInFuture)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsByteArray();

        var responseObj = objectMapper.readValue(response, CandidateAvailabilityResponse.class);

        // Then
        then(getCandidateAvailabilityService).should().get(anyString(), anyList(), anyInt());

        assertThat(responseObj).isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnCandidateAvailabilitySuccessfullyBySettingDefaultValueToNonMandatoryParams_GETAvailability() throws Exception {
        // Given
        var candidate = randomAlphanumeric(10);
        var interviewer = randomAlphanumeric(10);

        given(getCandidateAvailabilityService.get(candidate, List.of(interviewer), 7)).willReturn(new CandidateAvailabilityResponse());

        // When
        mockMvc.perform(get(PATH_AVAILABILITY)
                .header(USERNAME_HEADER, candidate)
                .param("interviewers", interviewer))
                .andExpect(status().isOk());

        // Then
        then(getCandidateAvailabilityService).should().get(candidate, List.of(interviewer), 7);
    }

    @Test
    void shouldReturnBadRequestWhenMandatoryHeaderIsMissing_GETAvailability() throws Exception {
        // When
        mockMvc.perform(get(PATH_AVAILABILITY)
                .param("interviewers", "interviewer1,interviewer2"))
                .andExpect(status().isBadRequest());

        // Then
        then(getCandidateAvailabilityService).should(never()).get(anyString(), anyList(), anyInt());
    }

    @Test
    void shouldReturnBadRequestWhenMandatoryQueryParamIsMissing_GETAvailability() throws Exception {
        // When
        mockMvc.perform(get(PATH_AVAILABILITY)
                .header("username", "any"))
                .andExpect(status().isBadRequest());

        // Then
        then(getCandidateAvailabilityService).should(never()).get(anyString(), anyList(), anyInt());
    }
}