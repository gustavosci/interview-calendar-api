/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.exception.ForbiddenUserException;
import com.gsantos.calendar.interview.exception.SlotOverlappedException;
import com.gsantos.calendar.interview.fixtures.AvailabilityRequestBuilder;
import com.gsantos.calendar.interview.fixtures.UserDDBBuilder;
import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.model.ddb.UserDDB;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SetAvailabilityTest {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    private SetAvailabilityImplService service;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldSetAvailabilitySuccessfully(){
        // Given
        var username = randomAlphanumeric(10);
        var userDDB = UserDDBBuilder.random(UserDDB.UserType.valueOf(service.getUserType().name()));

        var slotRequests1 = List.of(
                new AvailabilityRequest.SlotRequest(LocalTime.of(8, 0, 0), LocalTime.of(9, 0, 0)),
                new AvailabilityRequest.SlotRequest(LocalTime.of(10, 0, 0), LocalTime.of(11, 0, 0))
        );
        var dateSlotRequest1 = new AvailabilityRequest.DateSlotsRequest(LocalDate.now(), slotRequests1);

        var slotRequests2 = List.of(
                new AvailabilityRequest.SlotRequest(LocalTime.of(8, 0, 0), LocalTime.of(9, 0, 0)),
                new AvailabilityRequest.SlotRequest(LocalTime.of(10, 0, 0), LocalTime.of(11, 0, 0))
        );
        var dateSlotRequest2 = new AvailabilityRequest.DateSlotsRequest(LocalDate.now().plusDays(1), slotRequests2);

        var request = new AvailabilityRequest(List.of(dateSlotRequest1, dateSlotRequest2));

        var slotsDDB2 = new ArrayList<>(List.of(
                new CalendarDDB.SlotDDB(LocalTime.of(15, 0, 0), LocalTime.of(19, 0, 0))
        ));
        var calendarDDB2 = new CalendarDDB(username, dateSlotRequest2.getDate().format(LOCAL_DATE_FORMATTER), slotsDDB2);

        given(userRepository.getUserByUsername(username)).willReturn(Optional.of(userDDB));
        given(calendarRepository.getCalendarByUserAndDate(username, dateSlotRequest1.getDate())).willReturn(Optional.empty());
        given(calendarRepository.getCalendarByUserAndDate(username, dateSlotRequest2.getDate())).willReturn(Optional.of(calendarDDB2));

        // When
        service.set(username, request);

        // Then
        then(userRepository).should().getUserByUsername(any());
        then(calendarRepository).should(times(2)).getCalendarByUserAndDate(any(), any());
        then(calendarRepository).should(times(2)).save(any());
    }

    @TestFactory
    Stream<DynamicTest> shouldNotSaveCalendarWhenWhenThereAreOverlappedSlots() {
        return Stream.of(
                new CalendarDDB.SlotDDB(LocalTime.of(8, 0, 0), LocalTime.of(9, 0, 0)),
                new CalendarDDB.SlotDDB(LocalTime.of(7, 0, 0), LocalTime.of(9, 0, 0)),
                new CalendarDDB.SlotDDB(LocalTime.of(10, 0, 0), LocalTime.of(14, 0, 0)),
                new CalendarDDB.SlotDDB(LocalTime.of(12, 0, 0), LocalTime.of(13, 0, 0)),
                new CalendarDDB.SlotDDB(LocalTime.of(14, 0, 0), LocalTime.of(18, 0, 0))
        ).map(overlappedSlot -> DynamicTest.dynamicTest(String.format("Should throw error when there are overlapped slots: %s", overlappedSlot), () -> {
            // Given
            var username = randomAlphanumeric(10);
            var userDDB = UserDDBBuilder.random(UserDDB.UserType.valueOf(service.getUserType().name()));

            var slotRequests1 = List.of(
                    new AvailabilityRequest.SlotRequest(LocalTime.of(8, 0, 0), LocalTime.of(9, 0, 0)),
                    new AvailabilityRequest.SlotRequest(LocalTime.of(10, 0, 0), LocalTime.of(11, 0, 0))
            );
            var dateSlotRequest1 = new AvailabilityRequest.DateSlotsRequest(LocalDate.now(), slotRequests1);

            var slotRequests2 = List.of(
                    new AvailabilityRequest.SlotRequest(LocalTime.of(8, 0, 0), LocalTime.of(9, 0, 0)),
                    new AvailabilityRequest.SlotRequest(LocalTime.of(10, 0, 0), LocalTime.of(15, 0, 0))
            );
            var dateSlotRequest2 = new AvailabilityRequest.DateSlotsRequest(LocalDate.now().plusDays(1), slotRequests2);

            var request = new AvailabilityRequest(List.of(dateSlotRequest1, dateSlotRequest2));

            var slotsDDB2 = List.of(overlappedSlot);
            var calendarDDB2 = new CalendarDDB(username, dateSlotRequest2.getDate().format(LOCAL_DATE_FORMATTER), slotsDDB2);

            given(userRepository.getUserByUsername(username)).willReturn(Optional.of(userDDB));
            given(calendarRepository.getCalendarByUserAndDate(username, dateSlotRequest1.getDate())).willReturn(Optional.empty());
            given(calendarRepository.getCalendarByUserAndDate(username, dateSlotRequest2.getDate())).willReturn(Optional.of(calendarDDB2));

            // When
            Assertions.assertThrows(SlotOverlappedException.class, () -> service.set(username, request));

            // Then
            then(calendarRepository).should(never()).save(any());
        }));
    }

    @Test
    void shouldThrowErrorWhenUserDoesNotExist(){
        // Given
        var username = randomAlphanumeric(10);
        var request = AvailabilityRequestBuilder.random();

        given(userRepository.getUserByUsername(username)).willReturn(Optional.empty());

        // When
        Assertions.assertThrows(ForbiddenUserException.class, () -> service.set(username, request));

        // Then
        then(userRepository).should().getUserByUsername(any());
        then(calendarRepository).should(never()).save(any());
        then(calendarRepository).should(never()).getCalendarByUserAndDate(any(), any());
    }

    @Test
    void shouldThrowErrorWhenUserIsNotExpectedType(){
        // Given
        var username = randomAlphanumeric(10);
        var request = AvailabilityRequestBuilder.random();
        var userType = service.getUserType() == UserType.CANDIDATE ? UserDDB.UserType.INTERVIEWER : UserDDB.UserType.CANDIDATE;
        var userDDB = UserDDBBuilder.random(userType);

        given(userRepository.getUserByUsername(username)).willReturn(Optional.of(userDDB));

        // When
        Assertions.assertThrows(ForbiddenUserException.class, () -> service.set(username, request));

        // Then
        then(userRepository).should().getUserByUsername(any());
        then(calendarRepository).should(never()).save(any());
        then(calendarRepository).should(never()).getCalendarByUserAndDate(any(), any());
    }

    private static class SetAvailabilityImplService extends SetAvailabilityService {

        private final UserType userType;

        public SetAvailabilityImplService(CalendarRepository calendarRepository, UserRepository userRepository) {
            super(calendarRepository, userRepository);
            this.userType = UserType.values()[nextInt(0, UserType.values().length)];
        }

        @Override
        protected UserType getUserType() {
            return this.userType;
        }
    }
}