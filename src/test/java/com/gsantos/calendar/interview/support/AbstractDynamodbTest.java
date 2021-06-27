/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.support;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

public abstract class AbstractDynamodbTest {

    private final static DockerImageName DYNAMODB_LOCAL_IMAGE = DockerImageName.parse("amazon/dynamodb-local:1.15.0");
    private final static GenericContainer DYNAMODB_CONTAINER = new GenericContainer(DYNAMODB_LOCAL_IMAGE).withExposedPorts(8000);

    protected DynamoDbClient startDynamodb() {
        DYNAMODB_CONTAINER.start();
        var dynamodbUrl = String.format("http://%s:%s", DYNAMODB_CONTAINER.getHost(), DYNAMODB_CONTAINER.getFirstMappedPort());

        return DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamodbUrl))
                .build();

    }

    protected void tearDown() {
        DYNAMODB_CONTAINER.stop();
    }
}