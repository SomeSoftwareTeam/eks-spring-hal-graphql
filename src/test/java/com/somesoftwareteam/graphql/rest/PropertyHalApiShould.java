package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.utility.HalResourceBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
class PropertyHalApiShould extends HalResourceBase<Property> {

    @Test
    public void createReadUpdateDelete() {
        Property property = new Property("some property", null, JacksonUtil.toJsonNode("{}"));
        GeometryFactory geometryFactory = new GeometryFactory();
        property.setLocation(geometryFactory.createPoint(new Coordinate(0, 0, 0)));
        createReadUpdateDelete(property, "properties");
    }
}
