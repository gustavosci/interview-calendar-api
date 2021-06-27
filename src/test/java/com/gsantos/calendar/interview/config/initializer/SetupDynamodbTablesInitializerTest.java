/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.config.initializer;

import com.gsantos.calendar.interview.support.AbstractDynamodbTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.TableStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetupDynamodbTablesInitializerTest extends AbstractDynamodbTest {

    private final static String CALENDAR_TABLE_NAME = "test-calendar";
    private final static String USER_TABLE_NAME = "test-user";

    private DynamoDbClient dynamoDbClient;
    private SetupDynamodbTablesInitializer initializer;

    @BeforeAll
    public void setup() {
        dynamoDbClient = startDynamodb();
        initializer = new SetupDynamodbTablesInitializer(dynamoDbClient, USER_TABLE_NAME, CALENDAR_TABLE_NAME);
    }

    @AfterAll
    public void tearDown() {
        super.tearDown();
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