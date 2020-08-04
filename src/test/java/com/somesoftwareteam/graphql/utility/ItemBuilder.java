package com.somesoftwareteam.graphql.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Item;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class ItemBuilder {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode attributes;
    private Item item;

    public ItemBuilder createNewItemWithDefaults() {
        attributes = objectMapper.createObjectNode();
        item = new Item();
        item.setName("my property");
        item.setOwner("google|12345");
        item.setAttributes(attributes);
        return this;
    }

    public ItemBuilder useAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public ItemBuilder useOwner(String owner) {
        item.setOwner(owner);
        return this;
    }

    public ItemBuilder useProperty(Property property) {
        item.setProperty(property);
        return this;
    }

    @Transactional
    public ItemBuilder persist() {
        entityManager.persist(item);
        return this;
    }

    public Item build() {
        return item;
    }
}
