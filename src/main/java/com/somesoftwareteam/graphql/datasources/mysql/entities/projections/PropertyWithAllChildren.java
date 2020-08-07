package com.somesoftwareteam.graphql.datasources.mysql.entities.projections;

import com.fasterxml.jackson.databind.JsonNode;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Club;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.locationtech.jts.geom.Point;
import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

/**
 * https://docs.spring.io/spring-data/rest/docs/3.3.x/reference/html/#projections-excerpts.projections
 */
@Projection(name = "all", types = {Property.class})
public interface PropertyWithAllChildren {

    JsonNode getAttributes();

    String getAddress();

    UUID getId();

    Point getLocation();

    String getName();

    Club getClub();
}