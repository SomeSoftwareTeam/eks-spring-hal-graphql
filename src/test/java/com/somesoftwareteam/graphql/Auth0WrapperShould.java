package com.somesoftwareteam.graphql;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.somesoftwareteam.graphql.datasources.auth0.Auth0Wrapper;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://auth0.com/docs/extensions/authorization-extension/v2/api-access
 * https://github.com/auth0/auth0-java
 */
public class Auth0WrapperShould extends IntegrationTestBase {

    @Autowired
    private Auth0Wrapper something;

    @Test
    public void getUsers() throws Auth0Exception {
        List<User> users = something.getAuth0Users();
        users.forEach(u -> System.out.println(u.getEmail()));
        assertThat(users).isNotEmpty();
    }
}
