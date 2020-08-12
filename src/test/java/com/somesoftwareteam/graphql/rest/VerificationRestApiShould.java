package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import org.junit.jupiter.api.Test;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
class VerificationRestApiShould extends RestApiResourceBase<Verification> {

    @Test
    public void createReadUpdateDelete() {
        Verification verification = verificationBuilder.createNewEntityWithDefaults().build();
        createReadUpdateDelete(verification, "verifications");
    }
}
