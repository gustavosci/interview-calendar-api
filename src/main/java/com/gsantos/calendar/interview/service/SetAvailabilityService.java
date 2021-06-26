/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.exception.ForbiddenUserException;
import com.gsantos.calendar.interview.exception.SlotOverlappedException;
import com.gsantos.calendar.interview.mapping.CalendarDDBMapper;
import com.gsantos.calendar.interview.mapping.SlotDDBMapper;
import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

public abstract class SetAvailabilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetAvailabilityService.class);

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final SlotDDBMapper slotDDBMapper;
    private final CalendarDDBMapper calendarDDBMapper;

    protected SetAvailabilityService(CalendarRepository calendarRepository, UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.slotDDBMapper = new SlotDDBMapper();
        this.calendarDDBMapper = new CalendarDDBMapper();
    }

    public void set(final String username, final AvailabilityRequest request) {
        LOGGER.info("Setting availability. Request: {}", request);

        validateUser(username);

        var storedCalendar = getCalendarsByDateAndValidateOverlapping(username, request);
        request.getAvailableSlotsByDate().forEach(slotsByDate ->
                Optional.ofNullable(storedCalendar.get(slotsByDate.getDate()))
                        .ifPresentOrElse(c -> {
                                    var slotsDDB = slotDDBMapper.apply(slotsByDate.getAvailableSlots());
                                    c.getAvailableSlots().addAll(slotsDDB);
                                    calendarRepository.save(c);
                                },
                                () -> {
                                    var calendarDDB = calendarDDBMapper.apply(username, slotsByDate);
                                    calendarRepository.save(calendarDDB);
                                })
        );
    }

    private void validateUser(final String username) {
        var user = userRepository.getUserByUsername(username);
        var isUserValid = user.stream().anyMatch(u -> u.getType().name().equals(getUserType().name()));
        if (!isUserValid) throw new ForbiddenUserException();
    }

    private HashMap<LocalDate, CalendarDDB> getCalendarsByDateAndValidateOverlapping(final String username, final AvailabilityRequest request) {
        var storedCalendar = new HashMap<LocalDate, CalendarDDB>();
        request.getAvailableSlotsByDate().forEach(slotsByDate -> {
            var calendar = calendarRepository.getCalendarByUserAndDate(username, slotsByDate.getDate());
            calendar.ifPresent(c -> {
                validateOverlap(c, slotsByDate);
                storedCalendar.put(slotsByDate.getDate(), c);
            });
        });
        return storedCalendar;
    }

    private void validateOverlap(final CalendarDDB currentCalendar, final AvailabilityRequest.DateSlotsRequest requestedSlots) {
        currentCalendar.getAvailableSlots().forEach(slot -> {
            var isInvalid = requestedSlots.getAvailableSlots()
                    .stream()
                    .anyMatch(requestedSlot -> !isValidSlot(slot, requestedSlot));
            if (isInvalid) {
                var message = String.format("Slot overlapped. Date: %s", requestedSlots.getDate());
                LOGGER.warn(message);
                throw new SlotOverlappedException(message);
            }
        });
    }

    private boolean isValidSlot(final CalendarDDB.SlotDDB currentSlot, final AvailabilityRequest.SlotRequest requestedSlot) {
        var currentSlotStart = currentSlot.getStartTime();
        var currentSlotEnd = currentSlot.getEndTime();
        var requestedSlotStart = requestedSlot.getStartTime();
        var requestedSlotEnd = requestedSlot.getEndTime();

        var isRequestedSlotStartValid = requestedSlotStart.isBefore(currentSlotStart) || !requestedSlotStart.isBefore(currentSlotEnd);
        var isRequestedSlotEndValid = !requestedSlotEnd.isAfter(currentSlotStart) || requestedSlotEnd.isAfter(currentSlotEnd);
        var isRequestedSlotNotInCurrentSlot = !(requestedSlotStart.isBefore(currentSlotStart) && requestedSlotEnd.isAfter(currentSlotEnd));

        return isRequestedSlotStartValid && isRequestedSlotEndValid && isRequestedSlotNotInCurrentSlot;
    }

    protected abstract UserType getUserType();
}
