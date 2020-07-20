package com.somesoftwareteam.graphql.graphql;

import java.util.List;

public class VerificationFilter {

    private List<Long> ids;
    private String q;

    public VerificationFilter(List<Long> ids, String q) {
        this.ids = ids;
        this.q = q;
    }

    public VerificationFilter() {
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
