package com.somesoftwareteam.graphql.resolvers;

import java.util.List;

public class PropertyFilter {

    private String q;
    private Long id;

    public PropertyFilter() {
    }

    public PropertyFilter(String q, Long id) {
        this.q = q;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
