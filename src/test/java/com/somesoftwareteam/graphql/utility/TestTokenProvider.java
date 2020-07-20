package com.somesoftwareteam.graphql.utility;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

public class TestTokenProvider {

    private static String token;

    public static String getToken() {

        if (!Objects.isNull(token)) return token;

        System.out.println("getting token");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("client_id", System.getenv("CLIENT_ID"));
        params.add("client_secret", System.getenv("CLIENT_SECRET"));
        params.add("audience", "https://api.somesoftwareteam.com");

        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("https://somesoftwareteam.auth0.com").build();

        String responseContentString = webTestClient
                .post()
                .uri("/oauth/token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(params)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(String.class).getResponseBody().blockFirst();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        token = jsonParser.parseMap(responseContentString).get("access_token").toString();
        return token;
    }
}
