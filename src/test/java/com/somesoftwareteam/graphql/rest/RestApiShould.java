package com.somesoftwareteam.graphql.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiShould {

    @LocalServerPort
    public int port;

    @Test
    public void passHealthCheckWithoutAuth() {
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        client.get().uri("actuator/health").exchange().expectStatus().is2xxSuccessful();
    }

    @Test
    public void sendUnauthorizedWhenNoToken() {
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        client.get().uri("/rest/fixtures").exchange().expectStatus().isUnauthorized();
        client.get().uri("/rest/properties").exchange().expectStatus().isUnauthorized();
        client.get().uri("/rest/verifications").exchange().expectStatus().isUnauthorized();
        client.get().uri("/rest/documents").exchange().expectStatus().isUnauthorized();
    }
}
