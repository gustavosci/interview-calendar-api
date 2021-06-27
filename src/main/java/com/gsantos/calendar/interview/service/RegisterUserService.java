/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.service;

import com.gsantos.calendar.interview.model.request.RegisterUserRequest;
import com.gsantos.calendar.interview.exception.ConflictUserException;
import com.gsantos.calendar.interview.mapping.UserDDBMapper;
import com.gsantos.calendar.interview.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserService.class);

    private final UserRepository userRepository;
    private final UserDDBMapper userDDBMapper;

    public RegisterUserService(UserRepository userRepository, UserDDBMapper userDDBMapper) {
        this.userRepository = userRepository;
        this.userDDBMapper = userDDBMapper;
    }

    public void registerUser(final RegisterUserRequest request) {
        LOGGER.info("Creating user {}", request);

        if (exists(request.getUsername())) throw new ConflictUserException();

        userRepository.createUser(userDDBMapper.apply(request));
    }

    private boolean exists(final String username) {
        return userRepository.getUserByUsername(username).isPresent();
    }
}
