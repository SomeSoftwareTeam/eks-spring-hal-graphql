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
import java.util.Set;

/**
 * https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#spatial
 */
@Entity
@Table(name = "property")
@TypeDef(name = "json-string", typeClass = JsonStringType.class)
@GraphQLType(description = "Land and/or structure at a location belonging to an owner")
public class Property {

    @CreationTimestamp
    private ZonedDateTime created;

    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER)
    Set<Fixture> fixtures;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Point location;

    private String name;

    private String owner;

    @Type(type = "json-string")
    private JsonNode attributes;

    @UpdateTimestamp
    private ZonedDateTime updated;

    public Property() {
    }

    public Property(String name, String owner, JsonNode attributes) {
        this.name = name;
        this.owner = owner;
        this.attributes = attributes;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Set<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(Set<Fixture> fixtures) {
        this.fixtures = fixtures;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode properties) {
        this.attributes = properties;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }
}
