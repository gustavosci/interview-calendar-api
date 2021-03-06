/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.controller;

import com.gsantos.calendar.interview.model.request.AvailabilityRequest;
import com.gsantos.calendar.interview.model.response.ErrorResponse;
import com.gsantos.calendar.interview.service.SetInterviewerAvailabilityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/interviewers")
public class InterviewerController {

    private final SetInterviewerAvailabilityService setInterviewerAvailabilityService;

    public InterviewerController(SetInterviewerAvailabilityService setInterviewerAvailabilityService) {
        this.setInterviewerAvailabilityService = setInterviewerAvailabilityService;
    }

    @PostMapping(value = "/availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Set the interviewer's available slots")
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
        setInterviewerAvailabilityService.set(username, request);
    }
}
