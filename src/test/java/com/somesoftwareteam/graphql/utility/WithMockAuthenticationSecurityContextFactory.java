package com.somesoftwareteam.graphql.utility;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Map;

/**
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-method-withsecuritycontext
 */
public class WithMockAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockAuthentication input) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication authentication = new TestingAuthenticationToken(
                new MockAuthenticationPrincipal(Map.of(
                        "sub", input.name())),
                new MockAuthenticationCredentials(Map.of(
                        "https://api.somesoftwareteam.com/claims/groups", input.groups(),
                        "sub", input.name())),
                input.authorities());

        context.setAuthentication(authentication);

        return context;

    }
}
