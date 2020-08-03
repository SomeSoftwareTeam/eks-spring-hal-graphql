package com.somesoftwareteam.graphql.datasources.mysql.repositories;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Security provides Spring Data integration that allows referring to the current user within your queries:
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#common-expressions
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#filtering-using-prefilter-and-postfilter
 */
@Repository
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    @PostFilter("hasPermission(filterObject, 'read')")
    List<Property> findByOwner(@Param(value = "owner") String owner, Pageable pageable);

    @NonNull
    @Query("select p from Property p where p.owner = ?#{ authentication.name }")
    Page<Property> findAll(@NonNull Pageable pageable);

    @NonNull
    @RestResource(exported = false)
    @Query("select p from Property p where p.owner = ?#{ authentication.name }")
    Page<Property> findAll(Specification<Property> specification, @NonNull Pageable pageable);

    @NonNull
    @PostAuthorize("hasPermission(returnObject.orElse(null), 'READ')")
    Optional<Property> findById(@NonNull Long id);

    @NonNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#property, 'WRITE')")
    Property save(@NonNull @Param("property") Property property);
}
