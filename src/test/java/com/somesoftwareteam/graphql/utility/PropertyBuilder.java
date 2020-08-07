package com.somesoftwareteam.graphql.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class PropertyBuilder {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GeometryFactory geometryFactory;

    private ObjectNode attributes;
    private Property property;

    public PropertyBuilder createNewPropertyWithDefaults() {
        attributes = objectMapper.createObjectNode();
        property = new Property();
        property.setAddress("135 Trenor Ln, Powells Point, NC 27966, USA");
        property.setAttributes(attributes);
        property.setLocation(geometryFactory.createPoint(new Coordinate(-75.833445, 36.1392822)));
        property.setName("my property");
        property.setOwnerId("google|12345");
        return this;
    }

    public PropertyBuilder useAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public PropertyBuilder useName(String name) {
        property.setName(name);
        return this;
    }

    @Transactional
    public PropertyBuilder persist() {
        entityManager.persist(property);
        return this;
    }

    public Property build() {
        return property;
    }
}
