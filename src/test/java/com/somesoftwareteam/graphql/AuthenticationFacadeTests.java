package com.somesoftwareteam.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationFacadeTests extends com.somesoftwareteam.graphql.IntegrationTestBase {

    @Test
    @WithMockUser("google|12345")
    public void authenticationFacade_GetsCurrentPrincipalName() {
        String currentPrincipalName = authenticationFacade.getCurrentPrincipalName();
        assertThat(currentPrincipalName).isEqualTo("google|12345");
    }
}
