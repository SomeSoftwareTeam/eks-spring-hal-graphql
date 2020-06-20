package com.somesoftwareteam.graphql.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * https://www.baeldung.com/get-user-in-spring-security
 */
@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getCurrentPrincipalName() {
        Authentication authentication = getAuthentication();
        return authentication.getName();
    }
}