package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.utility.RestResourceBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 */
class PropertyRestApiShould extends RestResourceBase<Property> {

    @Test
    @AutoConfigureWebTestClient(timeout = "10000")
    public void createReadUpdateDelete() {
        Property property = new Property("some property", null, JacksonUtil.toJsonNode("{}"));
        createReadUpdateDelete(property, Property.class, "properties");
    }
}
