package com.somesoftwareteam.graphql.repositories;

import com.somesoftwareteam.graphql.entities.Verification;
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
@PreAuthorize("hasAuthority('SCOPE_read:verifications')")
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    // https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
    @NotNull
    @Query("select p from Verification p where p.owner = ?#{ authentication.name }")
    Page<Verification> findAll(@NotNull Pageable pageable);

    @NotNull
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Verification> findById(@NotNull Long id);

    @NotNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#verification, 'WRITE')")
    Verification save(@NotNull @Param("verification") Verification verification);
}
