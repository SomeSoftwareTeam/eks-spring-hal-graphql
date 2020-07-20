package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.somesoftwareteam.graphql.utility.TestTokenProvider.getToken;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class FixtureRestApiShould extends IntegrationTestBase {

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void createReadUpdateDelete() {

        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        String token = getToken();

        Fixture fixture = new Fixture("TestEntity", null, JacksonUtil.toJsonNode("{}"));
        Fixture resultFromPost = webTestClient
                .post()
                .uri("/rest/fixtures")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(fixture)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Fixture.class)
                .getResponseBody()
                .blockFirst();
        assertThat(resultFromPost).isNotNull();
        assertThat(resultFromPost.getOwner()).isNotNull();

        List<Fixture> resultFromGetAll = webTestClient
                .get()
                .uri("/rest/fixtures")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Fixture.class)
                .returnResult()
                .getResponseBody();
        assertThat(resultFromGetAll).isNotNull();
        assertThat(resultFromGetAll.size()).isGreaterThan(0);

//        String fixtureUri = "http://localhost:" + port + "/rest/fixtures/" + resultFromPost.getId();
//        String verificationUri = "http://localhost:" + port + "/rest/verifications/" + resultFromVerificationPost.getId();
//        String propertyUri = "http://localhost:" + port + "/rest/properties/" + resultFromPropertyPost.getId();

//        webTestClient
//                .put()
//                .uri(propertyUri + "/fixtures")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                .header(HttpHeaders.CONTENT_TYPE, "text/uri-list")
//                .bodyValue(fixtureUri)
//                .exchange()
//                .expectStatus()
//                .isNoContent();
    }
}
