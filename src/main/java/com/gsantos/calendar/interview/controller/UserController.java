/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.controller;

import com.gsantos.calendar.interview.domain.request.RegisterUserRequest;
import com.gsantos.calendar.interview.domain.response.ErrorResponse;
import com.gsantos.calendar.interview.service.RegisterUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final RegisterUserService registerUserService;

    public UserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Register User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "Created"
                    ),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request",
                            response = ErrorResponse.class
                    ),
                    @ApiResponse(
                            code = 409,
                            message = "Conflict"
                    ),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error",
                            response = ErrorResponse.class
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody final RegisterUserRequest request) {
        registerUserService.registerUser(request);
    }
}
