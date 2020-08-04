package com.somesoftwareteam.graphql.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class FixtureBuilder {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode attributes;
    private Fixture fixture;

    public FixtureBuilder createNewFixtureWithDefaults() {
        attributes = objectMapper.createObjectNode();
        fixture = new Fixture();
        fixture.setName("my property");
        fixture.setOwner("google|12345");
        fixture.setAttributes(attributes);
        return this;
    }

    public FixtureBuilder useAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public FixtureBuilder useOwner(String owner) {
        fixture.setOwner(owner);
        return this;
    }

    public FixtureBuilder useProperty(Property property) {
        fixture.setProperty(property);
        return this;
    }

    @Transactional
    public FixtureBuilder persist() {
        entityManager.persist(fixture);
        return this;
    }

    public Fixture build() {
        return fixture;
    }
}
