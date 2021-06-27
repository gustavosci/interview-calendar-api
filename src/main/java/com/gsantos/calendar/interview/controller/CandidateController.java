/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.controller;

import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import com.gsantos.calendar.interview.model.response.CandidateAvailableSlotsResponse;
import com.gsantos.calendar.interview.model.response.ErrorResponse;
import com.gsantos.calendar.interview.service.GetCandidateAvailabilityService;
import com.gsantos.calendar.interview.service.SetCandidateAvailabilityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/v1/candidates")
public class CandidateController {

    private final SetCandidateAvailabilityService setCandidateAvailabilityService;
    private final GetCandidateAvailabilityService getCandidateAvailabilityService;

    public CandidateController(SetCandidateAvailabilityService setCandidateAvailabilityService, GetCandidateAvailabilityService getCandidateAvailabilityService) {
        this.setCandidateAvailabilityService = setCandidateAvailabilityService;
        this.getCandidateAvailabilityService = getCandidateAvailabilityService;
    }

    @PostMapping(value = "/availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Set the candidate's available slots")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Ok"
                    ),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request",
                            response = ErrorResponse.class
                    ),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found"
                    ),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error",
                            response = ErrorResponse.class
                    )
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public void availability(@RequestHeader("username") final String username, @Valid @RequestBody final AvailabilityRequest request) {
        setCandidateAvailabilityService.set(username, request);
    }

    @GetMapping(value = "/available-slots", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get the candidate's available slots filtering by interviewer")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Ok"
                    ),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request",
                            response = ErrorResponse.class
                    ),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found"
                    ),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error",
                            response = ErrorResponse.class
                    )
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public CandidateAvailableSlotsResponse getAvailableSlots(@RequestHeader("username") final String username,
                                                             @RequestParam("interviewers") @NotEmpty final List<String> interviewers,
                                                             @RequestParam(name = "daysInFuture", required = false, defaultValue = "7") final Integer daysInFuture) {
        return getCandidateAvailabilityService.get(username, interviewers, daysInFuture);
    }
}
