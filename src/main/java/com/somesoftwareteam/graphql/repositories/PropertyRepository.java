package com.somesoftwareteam.graphql.repositories;

import com.somesoftwareteam.graphql.entities.Property;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Security provides Spring Data integration that allows referring to the current user within your queries:
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#common-expressions
 */
@Repository
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @NotNull
    @Query("select p from Property p where p.owner = ?#{ authentication.name }")
    Page<Property> findAll(@NotNull Pageable pageable);

    @NotNull
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Property> findById(@NotNull Long id);

    @NotNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#property, 'WRITE')")
    Property save(@NotNull @Param("property") Property property);
}
