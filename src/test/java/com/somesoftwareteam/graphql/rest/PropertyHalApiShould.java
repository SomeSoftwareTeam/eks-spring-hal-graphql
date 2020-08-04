package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.utility.HalResourceBase;
import org.junit.jupiter.api.Test;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
class PropertyHalApiShould extends HalResourceBase<Property> {

    @Test
    public void createReadUpdateDelete() {
        Property property = propertyBuilder.createNewPropertyWithDefaults().build();
        createReadUpdateDelete(property, "properties");
    }
}
