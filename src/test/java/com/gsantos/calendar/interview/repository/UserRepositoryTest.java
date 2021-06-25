/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.repository;

import com.gsantos.calendar.interview.domain.ddb.UserDDB;
import com.gsantos.calendar.interview.fixtures.UserDDBBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.net.URI;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    private final static DockerImageName DYNAMODB_LOCAL_IMAGE = DockerImageName.parse("amazon/dynamodb-local:1.15.0");
    private final static GenericContainer DYNAMODB_CONTAINER = new GenericContainer(DYNAMODB_LOCAL_IMAGE).withExposedPorts(8000);
    private final static String USER_TABLE_NAME = "test-user";

    private UserRepository repository;

    @BeforeAll
    public void setup() {
        DYNAMODB_CONTAINER.start();
        var dynamodbUrl = String.format("http://%s:%s", DYNAMODB_CONTAINER.getHost(), DYNAMODB_CONTAINER.getFirstMappedPort());

        var dynamoDbClient= DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamodbUrl))
                .build();

        createUserTable(dynamoDbClient);

        var dynamodbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        var userDDBTable = dynamodbEnhancedClient.table(USER_TABLE_NAME, TableSchema.fromBean(UserDDB.class));

        repository = new UserRepository(userDDBTable);
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