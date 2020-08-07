package com.somesoftwareteam.graphql;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.somesoftwareteam.graphql.datasources.auth0.Auth0Wrapper;
import com.somesoftwareteam.graphql.datasources.auth0.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://auth0.com/docs/extensions/authorization-extension/v2/api-access
 * https://github.com/auth0/auth0-java
 */
@SpringBootTest
public class Auth0WrapperShould {

    @Autowired
    private Auth0Wrapper something;

    @Test
    public void getUsers() throws Auth0Exception {
        List<User> users = something.getAuth0CoreUsers();
        users.forEach(u -> System.out.println(u.getEmail()));
        assertThat(users).isNotEmpty();
    }

    @Test
    public void getGroups() throws IOException {
        List<Group> groups = something.getAuth0AuthorizationExtensionGroups();
        groups.forEach(g -> System.out.println(g.getName()));
        assertThat(groups).isNotEmpty();
    }
}
