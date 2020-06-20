package com.somesoftwareteam.graphql;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MyAclServiceTests extends IntegrationTestBase {

    @Test
    public void myAclService_CreatesNewSecurityIdentity() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        List<String> securityIdentities = myAclService.getAllSecurityIdentities();
        assertThat(securityIdentities.contains("google|12345"));
        System.out.println("here");
    }
}
