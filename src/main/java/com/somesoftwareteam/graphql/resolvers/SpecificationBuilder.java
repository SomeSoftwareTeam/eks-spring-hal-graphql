package com.somesoftwareteam.graphql.resolvers;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SpecificationBuilder<T> {

    public Specification<T> createSpecFromFilter(FixtureFilter filter) {

        Specification<T> spec = exists();

        if (!Objects.isNull(filter.getQ())) {
            spec = spec.and(nameContains(filter.getQ()));
        }

        return spec;
    }

    private Specification<T> exists() {
        return (root, query, cb) -> cb.conjunction();
    }

    private Specification<T> nameContains(String substring) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + substring + "%");
    }
}