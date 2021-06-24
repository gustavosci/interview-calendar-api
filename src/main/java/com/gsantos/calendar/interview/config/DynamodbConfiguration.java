/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.config;

import com.gsantos.calendar.interview.domain.ddb.CalendarDDB;
import com.gsantos.calendar.interview.domain.ddb.UserDDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamodbConfiguration {

    @Bean
    public DynamoDbEnhancedClient ddbEnhancedClient(@Value("${aws.dynamodb.url}") final String dynamodbUrl) {
        var dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamodbUrl))
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<CalendarDDB> calendarDDBTable(final DynamoDbEnhancedClient client,
                                                       @Value("${aws.dynamodb.tables.calendar}") final String calendarTableName) {
        return table(client, calendarTableName, CalendarDDB.class);
    }

    @Bean
    public DynamoDbTable<UserDDB> userDDBTable(final DynamoDbEnhancedClient client,
                                               @Value("${aws.dynamodb.tables.user}") final String userTableName) {
        return table(client, userTableName, UserDDB.class);
    }

    private <T> DynamoDbTable<T> table(final DynamoDbEnhancedClient client, final String tableName, final Class<T> beanClass) {
        return client.table(tableName, TableSchema.fromBean(beanClass));
    }
}
