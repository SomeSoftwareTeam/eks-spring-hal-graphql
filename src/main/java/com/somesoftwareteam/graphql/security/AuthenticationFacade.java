package com.somesoftwareteam.graphql.security;

import org.springframework.security.core.Authentication;

/**
 * https://www.baeldung.com/get-user-in-spring-security
 */
public interface AuthenticationFacade {
    Authentication getAuthentication();
    String getCurrentPrincipalName();
}
