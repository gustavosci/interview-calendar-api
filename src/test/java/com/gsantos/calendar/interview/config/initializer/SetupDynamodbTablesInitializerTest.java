/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.config.initializer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.TableStatus;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetupDynamodbTablesInitializerTest {

    private final static DockerImageName DYNAMODB_LOCAL_IMAGE = DockerImageName.parse("amazon/dynamodb-local:1.15.0");
    private final GenericContainer DYNAMODB_CONTAINER = new GenericContainer(DYNAMODB_LOCAL_IMAGE).withExposedPorts(8000);

    private final static String CALENDAR_TABLE_NAME = "test-calendar";
    private final static String USER_TABLE_NAME = "user-calendar";

    private DynamoDbClient dynamoDbClient;
    private SetupDynamodbTablesInitializer initializer;

    @BeforeAll
    public void setup() {
        DYNAMODB_CONTAINER.start();
        String dynamodbUrl = String.format("http://%s:%s", DYNAMODB_CONTAINER.getHost(), DYNAMODB_CONTAINER.getFirstMappedPort());
        dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamodbUrl))
                .build();

        initializer = new SetupDynamodbTablesInitializer(dynamoDbClient, USER_TABLE_NAME, CALENDAR_TABLE_NAME);
    }

    @Test
    void shouldCreateTables() {
        // Given
        Assertions.assertThrows(ResourceNotFoundException.class, () -> describeTable(CALENDAR_TABLE_NAME));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> describeTable(USER_TABLE_NAME));

        // When
        initializer.afterPropertiesSet();

        // Then
        var calendarResponse = describeTable(CALENDAR_TABLE_NAME);
        assertThat(calendarResponse.table().tableStatus()).isEqualTo(TableStatus.ACTIVE);

        var userResponse = describeTable(USER_TABLE_NAME);
        assertThat(userResponse.table().tableStatus()).isEqualTo(TableStatus.ACTIVE);
    }

    private DescribeTableResponse describeTable(final String tableName) {
        var request = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();

        return dynamoDbClient.describeTable(request);
    }
}