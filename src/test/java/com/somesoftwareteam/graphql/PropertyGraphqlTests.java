package com.somesoftwareteam.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.somesoftwareteam.graphql.entities.Fixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.io.IOException;

import static com.somesoftwareteam.graphql.TestTokenProvider.getToken;
import static org.junit.jupiter.api.Assertions.*;

/**
 * https://www.baeldung.com/spring-boot-testing
 * https://www.baeldung.com/spring-jpa-test-in-memory-database
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 * https://github.com/graphql-java-kickstart/graphql-spring-boot/blob/master/example-graphql-tools/src/test/java/com/graphql/sample/boot/GraphQLToolsSampleApplicationTest.java
 */
class PropertyGraphqlTests extends IntegrationTestBase {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @Transactional
    @WithMockUser("QYVKjcojM1w3QvcNrhF6aEA0kendIznR@clients")
    public void propertyResource_CreateReadUpdateDelete() throws IOException {
        graphQLTestTemplate.addHeader("Authorization", "Bearer " + getToken());
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode createVariables = mapper.createObjectNode();
        createVariables.put("name", "some property");
        createVariables.put("attributes", "{}");
        GraphQLResponse createResponse = graphQLTestTemplate.perform("CreateProperty.graphql", createVariables);
        assertNotNull(createResponse);
        assertTrue(createResponse.isOk());

        ObjectNode getVariables = mapper.createObjectNode();
        getVariables.put("id", 1);
        GraphQLResponse response = graphQLTestTemplate.perform("GetProperty.graphql", createVariables);
        assertNotNull(response);
        assertTrue(response.isOk());

//        assertEquals(fixture.getId().toString(), response.get("$.data.fixture.id"));
    }
}
