package com.somesoftwareteam.graphql.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class VerificationBuilder {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode attributes;
    private Verification verification;

    public VerificationBuilder createNewVerificationWithDefaults() {
        attributes = objectMapper.createObjectNode();
        verification = new Verification();
        verification.setName("my property");
        verification.setOwner("google|12345");
        verification.setAttributes(attributes);
        return this;
    }

    public VerificationBuilder useAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public VerificationBuilder useOwner(String owner) {
        verification.setOwner(owner);
        return this;
    }

    public VerificationBuilder useProperty(Property property) {
        verification.setProperty(property);
        return this;
    }

    @Transactional
    public VerificationBuilder persist() {
        entityManager.persist(verification);
        return this;
    }

    public Verification build() {
        return verification;
    }
}
