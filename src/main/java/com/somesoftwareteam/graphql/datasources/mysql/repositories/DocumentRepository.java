package com.somesoftwareteam.graphql.datasources.mysql.repositories;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Security provides Spring Data integration that allows referring to the current user within your queries:
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#common-expressions
 */
@Repository
@PreAuthorize("hasAuthority('SCOPE_read:documents')")
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    @NonNull
    @Query("select d from Document d where d.owner = ?#{ authentication.name }")
    Page<Document> findAll(@NonNull Pageable pageable);

    @NonNull
    @RestResource(exported = false)
    @Query("select f from Document f where f.owner = ?#{ authentication.name }")
    Page<Document> findAll(Specification<Document> specification, @NonNull Pageable pageable);

    @NonNull
    @PostAuthorize("hasPermission(returnObject.orElse(null), 'READ')")
    Optional<Document> findById(@NonNull Long id);

    @NonNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#document, 'WRITE')")
    Document save(@NonNull @Param("document") Document document);
}
