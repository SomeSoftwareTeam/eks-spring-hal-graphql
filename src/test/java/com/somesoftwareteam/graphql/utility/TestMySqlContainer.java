package com.somesoftwareteam.graphql.utility;

import org.testcontainers.containers.MySQLContainer;

/**
 * https://www.testcontainers.org/test_framework_integration/junit_5/
 * https://www.baeldung.com/spring-boot-testcontainers-integration-test
 */
public class TestMySqlContainer extends MySQLContainer<TestMySqlContainer> {

    private static final String IMAGE_VERSION = "mysql:8.0.20";
    private static TestMySqlContainer container;

    private TestMySqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TestMySqlContainer getInstance() {
        if (container == null) {

            // https://github.com/testcontainers/testcontainers-java/issues/2627
            container = new TestMySqlContainer()
                    .withUsername("root")
                    .withPassword("test")
                    .withEnv("MYSQL_ROOT_PASSWORD", "test");

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
