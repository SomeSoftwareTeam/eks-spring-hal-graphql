package com.somesoftwareteam.graphql.acl;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * https://docs.spring.io/spring-security/site/docs/3.0.x/reference/domain-acls.html
 * https://github.com/spring-projects/spring-security/tree/master/acl
 * https://www.baeldung.com/spring-security-acl
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler;

    public AclMethodSecurityConfiguration(MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler) {
        this.defaultMethodSecurityExpressionHandler = defaultMethodSecurityExpressionHandler;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return defaultMethodSecurityExpressionHandler;
    }
}