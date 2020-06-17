package com.somesoftwareteam.graphql.resolvers;

public class VerificationFilter {

    private Long id;
    private String q;

    public VerificationFilter() {
    }

    public VerificationFilter(String q) {
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
