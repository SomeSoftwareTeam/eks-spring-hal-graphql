package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
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
class VerificationRestApiShould extends IntegrationTestBase {

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void createReadUpdateDelete() {

        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        String token = getToken();

        Verification verification = new Verification("TestVerification", null, JacksonUtil.toJsonNode("{}"));
        Verification resultFromVerificationPost = webTestClient
                .post()
                .uri("/rest/verifications")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(verification)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Verification.class)
                .getResponseBody()
                .blockFirst();
        assertThat(resultFromVerificationPost).isNotNull();
        assertThat(resultFromVerificationPost.getName()).isNotNull();

        List<Verification> resultFromGetAll = webTestClient
                .get()
                .uri("/rest/verifications")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Verification.class)
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
