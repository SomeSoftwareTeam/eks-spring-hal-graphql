package com.somesoftwareteam.graphql.datasources.mysql.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * https://thorben-janssen.com/generate-uuids-primary-keys-hibernate/
 * https://www.maxenglander.com/2017/09/01/optimized-uuid-with-hibernate.html
 */
@Entity
@Table(name = "club")
@TypeDef(name = "json-string", typeClass = JsonStringType.class)
public class Club {

    @Type(type = "json-string")
    private JsonNode attributes;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<ClubMember> clubMembers;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @NotBlank(message = "Club description cannot be empty.")
    private String description;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @NotBlank(message = "Club name cannot be empty.")
    private String name;

    @NotBlank(message = "Club owner id cannot be empty.")
    private String ownerId;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private Set<Property> properties;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }

    public Set<ClubMember> getClubMembers() {
        return clubMembers;
    }

    public void setClubMembers(Set<ClubMember> clubMembers) {
        this.clubMembers = clubMembers;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updated) {
        this.updatedAt = updatedAt;
    }
}
