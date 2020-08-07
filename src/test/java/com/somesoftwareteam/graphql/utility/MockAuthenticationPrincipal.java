package com.somesoftwareteam.graphql.utility;

/**
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-method-withsecuritycontext
 */
public class MockAuthenticationPrincipal {

    private Object claims;

    public MockAuthenticationPrincipal(Object claims) {
        this.claims = claims;
    }

    public Object getClaims() {
        return claims;
    }

    public void setClaims(Object claims) {
        this.claims = claims;
    }
}
