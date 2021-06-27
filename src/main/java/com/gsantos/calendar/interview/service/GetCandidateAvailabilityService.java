/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.mapping.CandidateAvailableSlotsResponseMapper;
import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.model.domain.DateSlots;
import com.gsantos.calendar.interview.model.domain.Slot;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.model.response.CandidateAvailableSlotsResponse;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.utils.DateConverterUtil;
import com.gsantos.calendar.interview.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class GetCandidateAvailabilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCandidateAvailabilityService.class);

    private final UserValidator userValidator;
    private final CalendarRepository calendarRepository;
    private final CandidateAvailableSlotsResponseMapper candidateAvailableSlotsResponseMapper;

    public GetCandidateAvailabilityService(UserValidator userValidator, CalendarRepository calendarRepository) {
        this.userValidator = userValidator;
        this.calendarRepository = calendarRepository;
        this.candidateAvailableSlotsResponseMapper = new CandidateAvailableSlotsResponseMapper();
    }

    public CandidateAvailableSlotsResponse get(final String username, final List<String> interviewers, final int daysInFuture) {
        LOGGER.info("Getting candidate availability. User: {} - Interviewers: {}", username, interviewers);

        userValidator.validate(username, UserType.CANDIDATE);
        interviewers.forEach(i -> userValidator.validateUserType(i, UserType.INTERVIEWER));

        var now = LocalDate.now();
        var candidateAvailability = calendarRepository.getUserAvailability(username, now, now.plusDays(daysInFuture))
                .stream()
                .collect(toMap(CalendarDDB::getDate, Function.identity()));
        var interviewersAvailability = getInterviewersAvailabilityByDate(interviewers, candidateAvailability.keySet());

        var matchesByInterviewer = new HashMap<String, List<DateSlots>>();
        interviewers.forEach(i -> {
            var interviewerMatches = Optional.ofNullable(interviewersAvailability.get(i))
                    .map(iAvailability -> getMatchedSlotsByDate(iAvailability, candidateAvailability))
                    .orElseGet(List::of);
            matchesByInterviewer.put(i, interviewerMatches);
        });

        return candidateAvailableSlotsResponseMapper.apply(username, matchesByInterviewer);
    }

    private Map<String, List<CalendarDDB>> getInterviewersAvailabilityByDate(final List<String> interviewers, final Set<String> candidateAvailabilityDates) {
        return candidateAvailabilityDates.stream().flatMap(candidateAvailabilityDate -> {
            var cDate = DateConverterUtil.toLocalDate(candidateAvailabilityDate);
            return interviewers.stream()
                    .map(i -> calendarRepository.getCalendarByUserAndDate(i, cDate));
        }).filter(Optional::isPresent)
                .map(Optional::get)
                .collect(groupingBy(CalendarDDB::getUser));
    }

    private List<DateSlots> getMatchedSlotsByDate(final List<CalendarDDB> interviewerAvailability,
                                                  final Map<String, CalendarDDB> candidateAvailability) {
        return interviewerAvailability.stream()
                .map(cInterviewer -> {
                    var candidateAvailabilityOnDate = candidateAvailability.get(cInterviewer.getDate());
                    var matchedSlots = matchedSlots(candidateAvailabilityOnDate.getAvailableSlots(), cInterviewer.getAvailableSlots())
                            .stream()
                            .map(ms -> new Slot(ms.getStartTime(), ms.getEndTime()))
                            .collect(toList());
                    return new DateSlots(DateConverterUtil.toLocalDate(cInterviewer.getDate()), matchedSlots);
                }).collect(toList());
    }

    private List<CalendarDDB.SlotDDB> matchedSlots(final List<CalendarDDB.SlotDDB> first, final List<CalendarDDB.SlotDDB> second) {
        return first.stream()
                .filter(f -> second.stream().anyMatch(f::equals))
                .collect(Collectors.toList());
    }
}