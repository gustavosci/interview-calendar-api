/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.exception.ForbiddenUserException;
import com.gsantos.calendar.interview.exception.UnexpectedUserTypeException;
import com.gsantos.calendar.interview.mapping.CandidateAvailabilityResponseMapper;
import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.model.domain.DateAvailability;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.model.response.CandidateAvailabilityResponse;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.utils.DateConverterUtil;
import com.gsantos.calendar.interview.validator.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GetCandidateAvailabilityServiceTest {

    @InjectMocks
    private GetCandidateAvailabilityService service;

    @Mock
    private UserValidator userValidator;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private CandidateAvailabilityResponseMapper candidateAvailabilityResponseMapper;

    @Test
    void shouldGetCandidateAvailabilitySuccessfully() {
        // Given
        var candidate = randomAlphanumeric(10);
        var interviewer1 = randomAlphanumeric(10);
        var interviewer2 = randomAlphanumeric(10);
        var interviewer3 = randomAlphanumeric(10);
        var daysInFuture = nextInt(1, 10);

        var dateFromCaptor = ArgumentCaptor.forClass(LocalDate.class);
        var dateToCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<Map<String, List<DateAvailability>>> matchesByInterviewerCaptor = ArgumentCaptor.forClass(Map.class);

        // Candidate mocks
        var candidateAvailabilitySlotsDate1 = List.of(
                new CalendarDDB.SlotDDB(LocalTime.now(), LocalTime.now().plusHours(1)),
                new CalendarDDB.SlotDDB(LocalTime.now(), LocalTime.now().plusHours(2))
        );
        var candidateAvailabilityDate1 = new CalendarDDB(candidate, DateConverterUtil.toString(LocalDate.now()), candidateAvailabilitySlotsDate1);

        var candidateAvailabilitySlotsDate2 = List.of(new CalendarDDB.SlotDDB(LocalTime.now().plusHours(3), LocalTime.now().plusHours(4)));
        var candidateAvailabilityDate2 = new CalendarDDB(candidate, DateConverterUtil.toString(LocalDate.now().plusDays(5)), candidateAvailabilitySlotsDate2);

        given(calendarRepository.getUserAvailability(eq(candidate), any(), any())).willReturn(List.of(candidateAvailabilityDate1, candidateAvailabilityDate2));

        // Interviewer 1 mocks
        var interviewer1AvailabilityDate2 = new CalendarDDB(interviewer1, candidateAvailabilityDate2.getDate(), candidateAvailabilitySlotsDate2);

        given(calendarRepository.getCalendarByUserAndDate(interviewer1, DateConverterUtil.toLocalDate(candidateAvailabilityDate1.getDate())))
                .willReturn(Optional.empty());
        given(calendarRepository.getCalendarByUserAndDate(interviewer1, DateConverterUtil.toLocalDate(candidateAvailabilityDate2.getDate())))
                .willReturn(Optional.of(interviewer1AvailabilityDate2));

        // Interviewer 2 mocks
        var interviewer2AvailabilityDate1 = new CalendarDDB(interviewer2, candidateAvailabilityDate1.getDate(), List.of(candidateAvailabilitySlotsDate1.get(0)));
        var interviewer2AvailabilityDate2 = new CalendarDDB(interviewer2, candidateAvailabilityDate2.getDate(), candidateAvailabilitySlotsDate2);

        given(calendarRepository.getCalendarByUserAndDate(interviewer2, DateConverterUtil.toLocalDate(candidateAvailabilityDate1.getDate())))
                .willReturn(Optional.of(interviewer2AvailabilityDate1));
        given(calendarRepository.getCalendarByUserAndDate(interviewer2, DateConverterUtil.toLocalDate(candidateAvailabilityDate2.getDate())))
                .willReturn(Optional.of(interviewer2AvailabilityDate2));

        // Interviewer 3 mocks
        given(calendarRepository.getCalendarByUserAndDate(eq(interviewer3), any())).willReturn(Optional.empty());

        // Mapper mock
        var responseMock = mock(CandidateAvailabilityResponse.class);
        given(candidateAvailabilityResponseMapper.apply(any(), any())).willReturn(responseMock);

        // When
        var response = service.get(candidate, List.of(interviewer1, interviewer2, interviewer3), daysInFuture);

        // Then
        then(userValidator).should().validate(candidate, UserType.CANDIDATE);
        then(userValidator).should().validateUserType(interviewer1, UserType.INTERVIEWER);
        then(userValidator).should().validateUserType(interviewer2, UserType.INTERVIEWER);
        then(userValidator).should().validateUserType(interviewer3, UserType.INTERVIEWER);
        then(calendarRepository).should().getUserAvailability(any(), dateFromCaptor.capture(), dateToCaptor.capture());
        then(calendarRepository).should(times(6)).getCalendarByUserAndDate(any(), any());
        then(candidateAvailabilityResponseMapper).should().apply(eq(candidate), matchesByInterviewerCaptor.capture());

        assertThat(response).isEqualTo(responseMock);
        assertThat(dateFromCaptor.getValue().plusDays(daysInFuture)).isEqualTo(dateToCaptor.getValue());

        assertThat(matchesByInterviewerCaptor.getValue().get(interviewer1)).hasSize(1);
        assertThat(matchesByInterviewerCaptor.getValue().get(interviewer2)).hasSize(2);
        assertThat(matchesByInterviewerCaptor.getValue().get(interviewer3)).isEmpty();
    }

    @Test
    void shouldValidateCandidateUser() {
        // Given
        var username = randomAlphanumeric(10);
        doThrow(ForbiddenUserException.class).when(userValidator).validate(username, UserType.CANDIDATE);

        // When
        Assertions.assertThrows(ForbiddenUserException.class, () -> service.get(username, List.of(), nextInt()));

        // Then
        then(userValidator).should(never()).validateUserType(any(), any());
        then(calendarRepository).should(never()).getCalendarByUserAndDate(any(), any());
        then(calendarRepository).should(never()).getUserAvailability(any(), any(), any());
        then(candidateAvailabilityResponseMapper).should(never()).apply(any(), any());
    }

    @Test
    void shouldValidateInterviewersUserType() {
        // Given
        var username = randomAlphanumeric(10);
        var interviewers = List.of(randomAlphanumeric(10), randomAlphanumeric(10));

        doThrow(UnexpectedUserTypeException.class).when(userValidator).validateUserType(interviewers.get(1), UserType.INTERVIEWER);

        // When
        Assertions.assertThrows(UnexpectedUserTypeException.class, () -> service.get(username, interviewers, nextInt()));

        // Then
        then(userValidator).should().validate(any(), any());
        then(userValidator).should().validateUserType(interviewers.get(0), UserType.INTERVIEWER);
        then(userValidator).should().validateUserType(interviewers.get(1), UserType.INTERVIEWER);
        then(calendarRepository).should(never()).getCalendarByUserAndDate(any(), any());
        then(calendarRepository).should(never()).getUserAvailability(any(), any(), any());
        then(candidateAvailabilityResponseMapper).should(never()).apply(any(), any());
    }

}