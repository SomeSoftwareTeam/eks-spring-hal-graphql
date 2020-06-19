package com.somesoftwareteam.graphql.repositories;

import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.entities.Verification;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PreAuthorize("hasAuthority('SCOPE_read:verifications')")
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    // https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
    @NotNull
    @Query("select v from Verification v where v.owner = ?#{ principal?.username }")
    Page<Verification> findAll(@NotNull Pageable pageable);

    // https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
    @NotNull
    @Query("select v from Verification v where v.owner = ?#{ principal?.username }")
    List<Verification> findAll();

    @NotNull
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Verification> findById(@NotNull Long id);

    @NotNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#verification, 'WRITE')")
    Verification save(@NotNull @Param("verification") Verification verification);
}
