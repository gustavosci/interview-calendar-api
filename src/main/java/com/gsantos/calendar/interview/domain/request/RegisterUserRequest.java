/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.domain.request;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RegisterUserRequest {

    public enum UserType {
        INTERVIEWER, CANDIDATE;
    }

    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private UserType type;

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
        RegisterUserRequest that = (RegisterUserRequest) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(name, that.name) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, type);
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
