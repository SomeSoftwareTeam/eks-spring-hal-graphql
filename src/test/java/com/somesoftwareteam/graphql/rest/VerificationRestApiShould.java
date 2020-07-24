package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import com.somesoftwareteam.graphql.utility.RestResourceBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class VerificationRestApiShould extends RestResourceBase<Verification> {

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void createReadUpdateDelete() {
        Verification verification = new Verification("TestVerification", null, JacksonUtil.toJsonNode("{}"));
        createReadUpdateDelete(verification, Verification.class, "verifications");
    }
}
