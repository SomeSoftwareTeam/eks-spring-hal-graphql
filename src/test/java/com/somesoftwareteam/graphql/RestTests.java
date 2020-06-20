package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.entities.Verification;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.somesoftwareteam.graphql.TestTokenProvider.getToken;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class RestTests extends IntegrationTestBase {

    @Test
    public void healthCheck_PassesWithoutAuth() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        webTestClient.get().uri("/health").exchange().expectStatus().is2xxSuccessful();
    }

    @Test
    public void restGetEndpoints_AreUnauthorizedWithoutToken() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        webTestClient.get().uri("/rest/fixtures").exchange().expectStatus().isUnauthorized();
        webTestClient.get().uri("/rest/verifications").exchange().expectStatus().isUnauthorized();
    }
}
