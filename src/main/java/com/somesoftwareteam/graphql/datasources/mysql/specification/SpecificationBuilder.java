package com.somesoftwareteam.graphql.datasources.mysql.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SpecificationBuilder<T> {

//    public Specification<T> createSpecFromFilter(DocumentFilter filter) {
//        if (Objects.isNull(filter)) return emptyConjunction();
//        return something(filter.getIds(), filter.getQ());
//    }

    private Specification<T> something(List<Long> ids, String q) {

        Specification<T> spec = emptyConjunction();

        if (!Objects.isNull(q)) {
            spec = spec.and(nameContains(q));
        }

        if (!Objects.isNull(ids)) {
            spec = spec.and(idIsIn(ids));
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