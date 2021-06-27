/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.repository;

import com.gsantos.calendar.interview.fixtures.CalendarDDBBuilder;
import com.gsantos.calendar.interview.model.ddb.CalendarDDB;
import com.gsantos.calendar.interview.support.AbstractDynamodbTest;
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
import software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.Projection;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalendarRepositoryTest extends AbstractDynamodbTest {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static String CALENDAR_TABLE_NAME = "test-calendar";

    private CalendarRepository repository;

    @BeforeAll
    public void setup() {
        var dynamoDbClient= startDynamodb();
        createCalendarTable(dynamoDbClient);

        var dynamodbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        var calendarDDBTable = dynamodbEnhancedClient.table(CALENDAR_TABLE_NAME, TableSchema.fromBean(CalendarDDB.class));

        repository = new CalendarRepository(calendarDDBTable);
    }

    @AfterAll
    public void tearDown() {
        super.tearDown();
    }

    @Test
    void shouldTryToGetCalendarByUserAndDateButNotFound() {
        // Given
        var username = randomAlphanumeric(10);
        var date = LocalDate.now();

        // When
        var response = repository.getCalendarByUserAndDate(username, date);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    void shouldSaveCalendarAndThenGetTheSame() {
        // Given
        var calendarDDB = CalendarDDBBuilder.random();

        // When
        repository.save(calendarDDB);

        // Then
        var storedCalendar = repository.getCalendarByUserAndDate(calendarDDB.getUser(), LocalDate.parse(calendarDDB.getDate(), LOCAL_DATE_FORMATTER));
        assertThat(storedCalendar)
                .isPresent()
                .contains(calendarDDB);
    }

    private void createCalendarTable(final DynamoDbClient dynamoDbClient) {
        var request = CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("user")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("date")
                                .attributeType(ScalarAttributeType.S)
                                .build())
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName("user")
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName("date")
                                .keyType(KeyType.RANGE)
                                .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
                .globalSecondaryIndexes(
                        GlobalSecondaryIndex.builder()
                                .indexName("date-index")
                                .keySchema(
                                        KeySchemaElement.builder()
                                                .attributeName("date")
                                                .keyType(KeyType.HASH)
                                                .build(),
                                        KeySchemaElement.builder()
                                                .attributeName("user")
                                                .keyType(KeyType.RANGE)
                                                .build()
                                )
                                .provisionedThroughput(ProvisionedThroughput.builder()
                                        .readCapacityUnits(10L)
                                        .writeCapacityUnits(10L)
                                        .build())
                                .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                                .build()

                )
                .tableName(CALENDAR_TABLE_NAME)
                .build();

        dynamoDbClient.createTable(request);
    }
}