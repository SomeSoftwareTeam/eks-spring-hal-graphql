package com.somesoftwareteam.graphql.utility;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://www.baeldung.com/spring-boot-testing
 * https://www.testcontainers.org/test_framework_integration/junit_5/
 * https://www.baeldung.com/spring-boot-testcontainers-integration-test
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 */
@SpringBootTest
public class IntegrationTestBase {

    @ClassRule
    public static final MySQLContainer<TestMySqlContainer> mySQLContainer = TestMySqlContainer.getInstance();

    @Autowired
    public DocumentBuilder documentBuilder;

    @Autowired
    public RecordBuilder itemBuilder;

    @Autowired
    public FixtureBuilder fixtureBuilder;

    @Autowired
    public MyAclService myAclService;

    @Autowired
    public PropertyBuilder propertyBuilder;

    @Autowired
    public AccessControlListBuilder accessControlListBuilder;

    @Autowired
    public VerificationBuilder verificationBuilder;

    @Test
    public void context_Loads() {
        String mySqlUrl = mySQLContainer.getJdbcUrl();
        assertThat(mySQLContainer.isRunning());
    }
}
