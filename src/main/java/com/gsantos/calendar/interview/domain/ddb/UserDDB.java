/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.domain.ddb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Objects;

@DynamoDbBean
public class UserDDB {
    public enum UserType {
        INTERVIEWER, CANDIDATE;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDDB userDDB = (UserDDB) o;
        return Objects.equals(username, userDDB.username) &&
                Objects.equals(name, userDDB.name) &&
                type == userDDB.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, type);
    }
}
