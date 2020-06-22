package com.somesoftwareteam.graphql.resolvers;

import java.util.List;

public class FixtureFilter {

    private List<Long> ids;
    private String q;

    public FixtureFilter(List<Long> ids, String q) {
        this.ids = ids;
        this.q = q;
    }

    public FixtureFilter() {
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
