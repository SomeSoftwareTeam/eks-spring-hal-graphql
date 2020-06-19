package com.somesoftwareteam.graphql.repositories;

import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.entities.Property;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PreAuthorize("hasAuthority('SCOPE_read:fixtures')")
public interface FixtureRepository extends JpaRepository<Fixture, Long>, JpaSpecificationExecutor<Fixture> {

    // https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
    @NotNull
    @Query("select f from Fixture f where f.owner = ?#{ principal?.username }")
    Page<Fixture> findAll(@NotNull Pageable pageable);

    // https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
    @NotNull
    @Query("select f from Fixture f where f.owner = ?#{ principal?.username }")
    List<Fixture> findAll();

    @NotNull
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Fixture> findById(@NotNull Long id);

    @NotNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#fixture, 'WRITE')")
    Fixture save(@NotNull @Param("fixture") Fixture fixture);
}
