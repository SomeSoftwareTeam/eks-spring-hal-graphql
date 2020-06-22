package com.somesoftwareteam.graphql.resolvers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class ResolverUtils {

    static Sort createSort(String sortOrder, String sortField) {
        if (Objects.isNull(sortOrder)) sortOrder = "ASC";
        if (Objects.isNull(sortField)) sortField = "id";
        return sortOrder.equals("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
    }

    static PageRequest createPageRequest(Integer page, Integer perPage, Sort sort) {
        if (Objects.isNull(page)) page = 0;
        if (Objects.isNull(perPage)) perPage = 10;
        return PageRequest.of(page, perPage, sort);
    }

    static PageRequest createPageRequest(Integer page, Integer perPage) {
        if (Objects.isNull(page)) page = 0;
        if (Objects.isNull(perPage)) perPage = 10;
        return PageRequest.of(page, perPage);
    }
}
