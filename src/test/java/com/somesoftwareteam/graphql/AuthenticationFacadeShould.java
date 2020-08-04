package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationFacadeShould extends IntegrationTestBase {

    @Autowired
    public AuthenticationFacade authenticationFacade;

    @Test
    @WithMockUser("google|12345")
    public void getCurrentPrincipalName() {
        String currentPrincipalName = authenticationFacade.getCurrentPrincipalName();
        assertThat(currentPrincipalName).isEqualTo("google|12345");
    }
}
