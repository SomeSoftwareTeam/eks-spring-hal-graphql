package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MyAclServiceShould extends IntegrationTestBase {

    @Test
    public void createNewSecurityIdentity() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        List<String> securityIdentities = myAclService.getAllSecurityIdentities();
        assertThat(securityIdentities.contains("google|12345"));
    }
}
