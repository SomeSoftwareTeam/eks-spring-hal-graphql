package com.somesoftwareteam.graphql.resolvers;

public class FixtureFilter {

    private Long id;
    private String q;

    public FixtureFilter() {
    }

    public FixtureFilter(String q) {
        this.q = q;
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
