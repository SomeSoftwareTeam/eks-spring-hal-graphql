package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class RestApiShould extends IntegrationTestBase {

    private WebTestClient webTestClient;

    @BeforeEach
    public void configureWebTestClient() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void passHealthCheckWithoutAuth() {
        webTestClient.get().uri("/health").exchange().expectStatus().is2xxSuccessful();
    }

    @Test
    public void sendUnauthorizedWhenNoToken() {
        webTestClient.get().uri("/rest/fixtures").exchange().expectStatus().isUnauthorized();
        webTestClient.get().uri("/rest/properties").exchange().expectStatus().isUnauthorized();
        webTestClient.get().uri("/rest/verifications").exchange().expectStatus().isUnauthorized();
    }
}
