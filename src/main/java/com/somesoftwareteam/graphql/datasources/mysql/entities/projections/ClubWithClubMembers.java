package com.somesoftwareteam.graphql.datasources.mysql.entities.projections;

import com.fasterxml.jackson.databind.JsonNode;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Club;
import com.somesoftwareteam.graphql.datasources.mysql.entities.ClubMember;
import org.springframework.data.rest.core.config.Projection;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * https://docs.spring.io/spring-data/rest/docs/3.3.x/reference/html/#projections-excerpts.projections
 */
@Projection(name = "clubMembers", types = {Club.class})
public interface ClubWithClubMembers {

    JsonNode getAttributes();

    ZonedDateTime getCreatedAt();

    String getDescription();

    Set<ClubMember> getClubMembers();

    String getName();

    UUID getId();

    String getOwnerId();

    ZonedDateTime getUpdatedAt();
}
