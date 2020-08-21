package com.somesoftwareteam.graphql.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class RecordBuilder {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode attributes;
    private Record record;

    public RecordBuilder createNewEntityWithDefaults() {
        attributes = objectMapper.createObjectNode();
        record = new Record();
        record.setName("my property");
        record.setOwnerId("google|12345");
        record.setAttributes(attributes);
        return this;
    }

    public RecordBuilder useAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public RecordBuilder useParent(Property property) {
        record.setParent(property);
        return this;
    }

    @Transactional
    public RecordBuilder persist() {
        entityManager.persist(record);
        return this;
    }

    public Record build() {
        return record;
    }
}
