package com.somesoftwareteam.graphql.graphql;

import io.leangen.graphql.annotations.types.GraphQLType;

@GraphQLType
public class ListMetadata {

    private Long count;

    public ListMetadata(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
