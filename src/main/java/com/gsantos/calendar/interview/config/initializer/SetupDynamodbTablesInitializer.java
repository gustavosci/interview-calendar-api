/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@Component
public class SetupDynamodbTablesInitializer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupDynamodbTablesInitializer.class);

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Value("${aws.dynamodb.tables.user}")
    private String userTableName;

    @Value("${aws.dynamodb.tables.calendar}")
    private String calendarTableName;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Create user DDB table if it doesn't exist
        try {
            describeTable(userTableName);
            LOGGER.info("User DDB table already exists - We don't need to create it.");
        } catch (ResourceNotFoundException ex) {
            LOGGER.info("User DDB table does not exist - Creating...");
            createUserTable();
        }

        // Create calendar DDB table if it doesn't exist
        try {
            describeTable(calendarTableName);
            LOGGER.info("Calendar DDB table already exists - We don't need to create it.");
        } catch (ResourceNotFoundException ex) {
            LOGGER.info("Calendar DDB table does not exist - Creating...");
            createCalendarTable();
        }
    }

    private void createCalendarTable() {
        var request = CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder()
                            .attributeName("date")
                            .attributeType(ScalarAttributeType.S)
                            .build(),
                        AttributeDefinition.builder()
                            .attributeName("user")
                            .attributeType(ScalarAttributeType.S)
                            .build())
                .keySchema(
                        KeySchemaElement.builder()
                            .attributeName("date")
                            .keyType(KeyType.HASH)
                            .build(),
                        KeySchemaElement.builder()
                            .attributeName("user")
                            .keyType(KeyType.RANGE)
                            .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
                .tableName(calendarTableName)
                .build();

        dynamoDbClient.createTable(request);
    }

    private void createUserTable() {
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
                .tableName(userTableName)
                .build();

        dynamoDbClient.createTable(request);
    }

    private void describeTable(final String tableName) {
        var request = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();

        dynamoDbClient.describeTable(request);
    }
}
