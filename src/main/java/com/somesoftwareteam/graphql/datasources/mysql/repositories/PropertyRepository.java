package com.somesoftwareteam.graphql.datasources.mysql.repositories;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

/**
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#common-expressions
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#filtering-using-prefilter-and-postfilter
 * https://docs.spring.io/spring-data/rest/docs/3.3.x/reference/html/#projections-excerpts.projections
 */
@RepositoryRestResource
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public interface PropertyRepository extends JpaRepository<Property, UUID>, JpaSpecificationExecutor<Property> {
}
