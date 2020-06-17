package com.somesoftwareteam.graphql.repositories;

import com.somesoftwareteam.graphql.entities.Fixture;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PreAuthorize("hasAuthority('SCOPE_read:fixtures')")
public interface FixtureRepository extends JpaRepository<Fixture, Long> {

    @NotNull
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Fixture> findAll();

    @NotNull
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Fixture> findById(@NotNull Long id);

    @NotNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#fixture, 'WRITE')")
    Fixture save(@NotNull @Param("fixture") Fixture fixture);
}
