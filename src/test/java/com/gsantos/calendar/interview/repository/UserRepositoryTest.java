/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.repository;

import com.gsantos.calendar.interview.fixtures.UserDDBBuilder;
import com.gsantos.calendar.interview.model.ddb.UserDDB;
import com.gsantos.calendar.interview.util.AbstractDynamodbTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest extends AbstractDynamodbTest {

    private final static String USER_TABLE_NAME = "test-user";

    private UserRepository repository;

    @BeforeAll
    public void setup() {
        var dynamoDbClient= startDynamodb();
        createUserTable(dynamoDbClient);

        var dynamodbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        var userDDBTable = dynamodbEnhancedClient.table(USER_TABLE_NAME, TableSchema.fromBean(UserDDB.class));

        repository = new UserRepository(userDDBTable);
    }

    @AfterAll
    public void tearDown() {
        super.tearDown();
    }

    @Test
    void shouldTryToGetUserByUsernameButNotFound() {
        // Given
        var username = randomAlphanumeric(10);

        // When
        var response = repository.getUserByUsername(username);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    void shouldRegisterUserAndThenGetTheSameByUsername() {
        // Given
        var userDDB = UserDDBBuilder.random();

        // When
        repository.createUser(userDDB);

        // Then
        var response = repository.getUserByUsername(userDDB.getUsername());
        assertThat(response)
                .isPresent()
                .contains(userDDB);
    }

    private void createUserTable(final DynamoDbClient dynamoDbClient) {
        var request = CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("username")
                                .attributeType(ScalarAttributeType.S)
                                .build())
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName("username")
                                .keyType(KeyType.HASH)
                                .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
                .tableName(USER_TABLE_NAME)
                .build();

        dynamoDbClient.createTable(request);
    }
}