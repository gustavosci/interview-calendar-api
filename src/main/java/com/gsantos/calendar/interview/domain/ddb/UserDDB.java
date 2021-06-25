/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.domain.ddb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class UserDDB {
    private String username;
    private String name;
    private UserType type;

    public UserDDB() {
    }

    public UserDDB(String username, String name, UserType type) {
        this.username = username;
        this.name = name;
        this.type = type;
    }

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public enum UserType {
        INTERVIEWER, CANDIDATE;
    }
}
