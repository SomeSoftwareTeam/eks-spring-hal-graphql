package com.somesoftwareteam.graphql.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.leangen.graphql.annotations.types.GraphQLType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "verification")
@TypeDef(name = "json-string", typeClass = JsonStringType.class)
@GraphQLType(description = "Verification of property or fixture ownership")
public class Verification {

    @CreationTimestamp
    private ZonedDateTime created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Type(type = "json-string")
    private JsonNode attributes;

    @OneToOne(fetch = FetchType.EAGER)
    private Property property;

    @UpdateTimestamp
    private ZonedDateTime updated;

    public Verification() {
    }

    public Verification(String name, JsonNode attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public Verification(String name, JsonNode attributes, Property property) {
        this.name = name;
        this.attributes = attributes;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode properties) {
        this.attributes = properties;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }
}
