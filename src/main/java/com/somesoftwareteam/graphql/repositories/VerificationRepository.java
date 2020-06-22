package com.somesoftwareteam.graphql.repositories;

import com.somesoftwareteam.graphql.entities.Verification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
@PreAuthorize("hasAuthority('SCOPE_read:verifications')")
public interface VerificationRepository extends JpaRepository<Verification, Long>, JpaSpecificationExecutor<Verification> {

    // https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
    @NonNull
    @Query("select p from Verification p where p.owner = ?#{ authentication.name }")
    Page<Verification> findAll(@NonNull Pageable pageable);

    @NonNull
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Verification> findById(@NonNull Long id);

    @NonNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#verification, 'WRITE')")
    Verification save(@NonNull @Param("verification") Verification verification);
}
