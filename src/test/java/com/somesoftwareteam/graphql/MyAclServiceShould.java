package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MyAclServiceShould extends IntegrationTestBase {

    @Autowired
    MyAclService myAclService;

    @Test
    public void createNewSecurityIdentity() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        List<String> securityIdentities = myAclService.getAllSecurityIdentities();
        assertThat(securityIdentities.contains("google|12345"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:properties"})
    public void givePermissionToNonOwner() {
        Property property = propertyBuilder.createNewPropertyWithDefaults().useGroupName("google|12345").persist().build();
        myAclService.createReadPermissionAccessControlEntry(property.getId(), "google|54321");
    }
}
