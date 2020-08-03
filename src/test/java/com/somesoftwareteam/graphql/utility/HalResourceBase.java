package com.somesoftwareteam.graphql.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.config.HypermediaWebTestClientConfigurer;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.somesoftwareteam.graphql.utility.TestTokenProvider.getToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HalResourceBase<T> {

    @LocalServerPort
    public int port;

    @Autowired
    public HypermediaWebTestClientConfigurer configurer;

    public void createReadUpdateDelete(T entity, String resourceCollectionName) {

        String token = getToken();

        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/rest/" + resourceCollectionName)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .apply(configurer)
                .build();

        client
                .post()
                .bodyValue(entity)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(new TypeReferences.EntityModelType<T>())
                .consumeWith(result -> {
                    EntityModel<T> model = result.getResponseBody();
                    assertThat(model).isNotNull();
//                    assertThat(model.getContent()).isNotNull();
                    assertThat(model.getRequiredLink(IanaLinkRelations.SELF)).isNotNull();
                });

        client
                .get()
                .accept(HAL_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new TypeReferences.CollectionModelType<EntityModel<T>>() {
                })
                .consumeWith(result -> {
                    CollectionModel<EntityModel<T>> model = result.getResponseBody();
                    assertThat(model).isNotNull();
                    assertThat(model.getRequiredLink(IanaLinkRelations.SELF)).isNotNull();
                });

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
