package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.utility.HalResourceBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
class FixtureRestApiShould extends HalResourceBase<Fixture> {

    @Test
    public void createReadUpdateDelete() {
        Fixture fixture = new Fixture("TestEntity", null, JacksonUtil.toJsonNode("{}"));
        createReadUpdateDelete(fixture, "fixtures");
    }
}
