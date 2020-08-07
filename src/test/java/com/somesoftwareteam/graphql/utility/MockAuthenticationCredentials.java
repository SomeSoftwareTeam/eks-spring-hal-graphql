package com.somesoftwareteam.graphql.utility;

/**
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-method-withsecuritycontext
 */
public class MockAuthenticationCredentials {

    private Object claims;

    public MockAuthenticationCredentials(Object claims) {
        this.claims = claims;
    }

    public Object getClaims() {
        return claims;
    }

    public void setClaims(Object claims) {
        this.claims = claims;
    }
}
