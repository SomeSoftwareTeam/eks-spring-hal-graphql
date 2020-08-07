package com.somesoftwareteam.graphql.datasources.mysql.repositories;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

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

    @Query("select p from Property p " +
            "where p.groupName in ?#{ authentication.credentials.claims['https://api.somesoftwareteam.com/claims/groups'] } " +
            "and p.name like %:input%")
    Page<Property> findByNameContains(@Param(value = "input") String input, Pageable pageable);

    @NonNull
    @Query("select p from Property p " +
            "where p.groupName in ?#{ authentication.credentials.claims['https://api.somesoftwareteam.com/claims/groups'] }")
    Page<Property> findAll(@NonNull Pageable pageable);

    @NonNull
    @Query("select p from Property p where p.id = :id " +
            "and p.groupName in ?#{ authentication.credentials.claims['https://api.somesoftwareteam.com/claims/groups'] }")
    Optional<Property> findById(@NonNull Long id);
}
