package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.utility.RestResourceBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class FixtureRestApiShould extends RestResourceBase<Fixture> {

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void createReadUpdateDelete() {
        Fixture fixture = new Fixture("TestEntity", null, JacksonUtil.toJsonNode("{}"));
        createReadUpdateDelete(fixture, Fixture.class, "fixtures");
    }
}
