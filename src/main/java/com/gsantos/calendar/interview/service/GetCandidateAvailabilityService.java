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
import com.gsantos.calendar.interview.model.domain.Slot;
import com.gsantos.calendar.interview.model.domain.UserType;
import com.gsantos.calendar.interview.model.response.CandidateAvailabilityResponse;
import com.gsantos.calendar.interview.repository.CalendarRepository;
import com.gsantos.calendar.interview.utils.DateConverterUtil;
import com.gsantos.calendar.interview.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class GetCandidateAvailabilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCandidateAvailabilityService.class);

    private final UserValidator userValidator;
    private final CalendarRepository calendarRepository;
    private final CandidateAvailabilityResponseMapper candidateAvailabilityResponseMapper;

    public GetCandidateAvailabilityService(UserValidator userValidator, CalendarRepository calendarRepository,
                                           CandidateAvailabilityResponseMapper candidateAvailabilityResponseMapper) {
        this.userValidator = userValidator;
        this.calendarRepository = calendarRepository;
        this.candidateAvailabilityResponseMapper = candidateAvailabilityResponseMapper;
    }

    public CandidateAvailabilityResponse get(final String username, final List<String> interviewers, final int daysInFuture) {
        LOGGER.info("Getting candidate availability. User: {} - Interviewers: {}", username, interviewers);

        validateRequest(username, interviewers);

        var now = LocalDate.now();
        var candidateAvailability = calendarRepository.getUserAvailability(username, now, now.plusDays(daysInFuture))
                .stream()
                .collect(toMap(CalendarDDB::getDate, Function.identity()));
        var interviewersAvailability = getInterviewersAvailabilityByDate(interviewers, candidateAvailability.keySet());

        var matchesByInterviewer = interviewers
                .stream()
                .collect(toMap(
                        Function.identity(),
                        interviewer -> Optional.ofNullable(interviewersAvailability.get(interviewer))
                                .map(iAvailability -> getMatchedSlotsByDate(iAvailability, candidateAvailability))
                                .orElseGet(List::of)
                        )
                );

        return candidateAvailabilityResponseMapper.apply(username, matchesByInterviewer);
    }

    private void validateRequest(final String username, final List<String> interviewers) {
        userValidator.validate(username, UserType.CANDIDATE, () -> {throw new ForbiddenUserException();});
        interviewers.forEach(i -> userValidator.validate(i, UserType.INTERVIEWER, () -> {
            var message = String.format("It's expected for the user %s to exist and be %s", i, UserType.INTERVIEWER);
            LOGGER.warn(message);
            throw new UnexpectedUserTypeException(message);
        }));
    }

    private Map<String, List<CalendarDDB>> getInterviewersAvailabilityByDate(final List<String> interviewers, final Set<String> candidateAvailabilityDates) {
        return candidateAvailabilityDates.stream()
                .flatMap(candidateAvailabilityDate -> {
                    var cDate = DateConverterUtil.toLocalDate(candidateAvailabilityDate);
                    return interviewers.stream().map(i -> calendarRepository.getCalendarByUserAndDate(i, cDate));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(groupingBy(CalendarDDB::getUser));
    }

    private List<DateAvailability> getMatchedSlotsByDate(final List<CalendarDDB> interviewerAvailability,
                                                         final Map<String, CalendarDDB> candidateAvailability) {
        return interviewerAvailability.stream()
                .map(cInterviewer -> {
                    var candidateAvailabilityOnDate = candidateAvailability.get(cInterviewer.getDate());
                    var matchedSlots = matchedSlots(candidateAvailabilityOnDate.getAvailableSlots(), cInterviewer.getAvailableSlots());
                    return new DateAvailability(DateConverterUtil.toLocalDate(cInterviewer.getDate()), matchedSlots);
                }).collect(toList());
    }

    private List<Slot> matchedSlots(final List<CalendarDDB.SlotDDB> first, final List<CalendarDDB.SlotDDB> second) {
        return first.stream()
                .filter(f -> second.stream().anyMatch(f::equals))
                .map(ms -> new Slot(ms.getStartTime(), ms.getEndTime()))
                .collect(toList());
    }
}
