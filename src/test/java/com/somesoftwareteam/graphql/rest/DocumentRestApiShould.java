package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import com.somesoftwareteam.graphql.utility.RestResourceBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.somesoftwareteam.graphql.utility.TestTokenProvider.getToken;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class DocumentRestApiShould extends RestResourceBase<Document> {

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void createReadUpdateDelete() {

        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        String token = getToken();

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource("somefile.txt"));
        multipartBodyBuilder.part("name", "some file");
        multipartBodyBuilder.part("description", "some description");

        Document resultFromPost = webTestClient
                .post()
                .uri("rest/documents")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Document.class)
                .getResponseBody()
                .blockFirst();
        assertThat(resultFromPost).isNotNull();

    }
}
