package com.somesoftwareteam.graphql.datasources.mysql.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#spatial
 * https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
 */
@Entity
@Table(name = "property")
@TypeDef(name = "json-string", typeClass = JsonStringType.class)
public class Property {

    @NotBlank(message = "Property address cannot be empty.")
    private String address;

    private boolean addressFormatted;

    @Type(type = "json-string")
    private JsonNode attributes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    private Club club;

    @Column(name = "club_id")
    private UUID clubId;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Document> documents;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Fixture> fixtures;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private Point location;

    @NotBlank(message = "Property name cannot be empty.")
    private String name;

    @NotBlank(message = "Property owner id cannot be empty.")
    private String ownerId;

    @OneToMany(mappedBy = "parent")
    private Set<Record> records;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public UUID getClubId() {
        return clubId;
    }

    public void setClubId(UUID clubId) {
        this.clubId = clubId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAddressFormatted() {
        return addressFormatted;
    }

    public void setAddressFormatted(boolean addressFormatted) {
        this.addressFormatted = addressFormatted;
    }

    public Set<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(Set<Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
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


    public Set<Record> getRecords() {
        return records;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
