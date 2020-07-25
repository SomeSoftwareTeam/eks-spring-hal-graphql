package com.somesoftwareteam.graphql.graphql;

import java.util.List;

public class DocumentFilter {

    private List<Long> ids;
    private String q;

    public DocumentFilter(List<Long> ids, String q) {
        this.ids = ids;
        this.q = q;
    }

    public DocumentFilter() {
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
