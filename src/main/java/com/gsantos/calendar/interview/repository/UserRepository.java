/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.repository;

import com.gsantos.calendar.interview.domain.ddb.UserDDB;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
public class UserRepository {

    private final DynamoDbTable<UserDDB> userDDBTable;

    public UserRepository(DynamoDbTable<UserDDB> userDDBTable) {
        this.userDDBTable = userDDBTable;
    }

    public void createUser(final UserDDB user) {
        userDDBTable.putItem(user);
    }

    public Optional<UserDDB> getUserByUsername(final String username) {
        var key = Key.builder().partitionValue(username).build();
        return Optional.ofNullable(userDDBTable.getItem(key));
    }
}
