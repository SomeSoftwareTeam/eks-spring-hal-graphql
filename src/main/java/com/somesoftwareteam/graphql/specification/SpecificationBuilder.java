package com.somesoftwareteam.graphql.specification;

import com.somesoftwareteam.graphql.resolvers.FixtureFilter;
import com.somesoftwareteam.graphql.resolvers.PropertyFilter;
import com.somesoftwareteam.graphql.resolvers.VerificationFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SpecificationBuilder<T> {

    public Specification<T> createSpecFromFilter(FixtureFilter filter) {

        Specification<T> spec = emptyConjunction();

        if (Objects.isNull(filter)) return spec;

        if (!Objects.isNull(filter.getQ())) {
            spec = spec.and(nameContains(filter.getQ()));
        }

        if (!Objects.isNull(filter.getIds())) {
            spec = spec.and(idIsIn(filter.getIds()));
        }

        return spec;
    }

    public Specification<T> createSpecFromFilter(PropertyFilter filter) {

        Specification<T> spec = emptyConjunction();

        if (Objects.isNull(filter)) return spec;

        if (!Objects.isNull(filter.getQ())) {
            spec = spec.and(nameContains(filter.getQ()));
        }

        if (!Objects.isNull(filter.getIds())) {
            spec = spec.and(idIsIn(filter.getIds()));
        }

        return spec;
    }

    public Specification<T> createSpecFromFilter(VerificationFilter filter) {

        Specification<T> spec = emptyConjunction();

        if (Objects.isNull(filter)) return spec;

        if (!Objects.isNull(filter.getQ())) {
            spec = spec.and(nameContains(filter.getQ()));
        }

        if (!Objects.isNull(filter.getIds())) {
            spec = spec.and(idIsIn(filter.getIds()));
        }

        return spec;
    }

    private Specification<T> emptyConjunction() {
        return (root, query, cb) -> cb.conjunction();
    }

    private Specification<T> nameContains(String substring) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + substring + "%");
    }

    private Specification<T> idIsIn(List<Long> ids) {
        return (root, query, cb) -> cb.in(root.get("id")).value(cb.literal(ids));
    }
}