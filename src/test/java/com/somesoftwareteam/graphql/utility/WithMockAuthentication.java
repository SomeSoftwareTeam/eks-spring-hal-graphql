package com.somesoftwareteam.graphql.utility;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-method-withsecuritycontext
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthenticationSecurityContextFactory.class)
public @interface WithMockAuthentication {

    String[] authorities() default {};

    String name() default "Thomas";

    String[] groups() default {};
}
