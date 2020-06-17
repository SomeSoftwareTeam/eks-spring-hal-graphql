package com.somesoftwareteam.graphql.repositories;

import com.somesoftwareteam.graphql.entities.Property;
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
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @NotNull
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Property> findAll();

    @NotNull
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Property> findById(@NotNull Long id);

    @NotNull
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#property, 'WRITE')")
    Property save(@NotNull @Param("property") Property property);
}
