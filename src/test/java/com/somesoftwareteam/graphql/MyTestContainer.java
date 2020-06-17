package com.somesoftwareteam.graphql;

import org.testcontainers.containers.MySQLContainer;

/**
 * https://www.testcontainers.org/test_framework_integration/junit_5/
 * https://www.baeldung.com/spring-boot-testcontainers-integration-test
 */
public class MyTestContainer extends MySQLContainer<MyTestContainer> {

    private static final String IMAGE_VERSION = "mysql:8.0.17";
    private static com.somesoftwareteam.graphql.MyTestContainer container;

    private MyTestContainer() {
        super(IMAGE_VERSION);
    }

    public static com.somesoftwareteam.graphql.MyTestContainer getInstance() {
        if (container == null) {
            container = new com.somesoftwareteam.graphql.MyTestContainer();
            container.start();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("SPRING_DATASOURCE_URL", container.getJdbcUrl());
        System.setProperty("SPRING_DATASOURCE_USERNAME", container.getUsername());
        System.setProperty("SPRING_DATASOURCE_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
