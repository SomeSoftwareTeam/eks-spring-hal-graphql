package com.somesoftwareteam.graphql.datasources.mysql.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "item")
@TypeDef(name = "json-string", typeClass = JsonStringType.class)
public class Item {

    @Type(type = "json-string")
    private JsonNode attributes;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private String ownerId;

    @ManyToOne
    @JoinColumn(name = "property_id", updatable = false, insertable = false)
    private Property property;

    @Column(name = "property_id")
    private UUID propertyId;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    public Item() {
    }

    public Item(String name, String groupId, JsonNode attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public Item(String name, String groupId, JsonNode attributes, Property property) {
        this.name = name;
        this.attributes = attributes;
        this.property = property;
    }

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
