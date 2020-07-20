package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class RestApiShould extends IntegrationTestBase {

    @Test
    public void passHealthCheckWithoutAuth() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        webTestClient.get().uri("/health").exchange().expectStatus().is2xxSuccessful();
    }

    @Test
    public void sendUnauthorizedWhenNoToken() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        webTestClient.get().uri("/rest/fixtures").exchange().expectStatus().isUnauthorized();
        webTestClient.get().uri("/rest/verifications").exchange().expectStatus().isUnauthorized();
    }
}
