package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.entities.Verification;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

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

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void postProperty_CreatesProperty() {

        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        String token = getToken();

        Property property = new Property("some property", "google|12345", JacksonUtil.toJsonNode("{}"));
        Property resultFromPropertyPost = webTestClient
                .post()
                .uri("/rest/properties")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(property)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Property.class)
                .getResponseBody()
                .blockFirst();
        assertThat(resultFromPropertyPost).isNotNull();
        assertThat(resultFromPropertyPost.getOwner()).isNotNull();

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

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void postFixture_CreatesFixture() {

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

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void postVerification_CreatesVerification() {

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
