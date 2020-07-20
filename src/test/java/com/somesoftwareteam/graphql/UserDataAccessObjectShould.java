package com.somesoftwareteam.graphql;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.somesoftwareteam.graphql.datasources.auth0.UserDataAccessObject;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDataAccessObjectShould extends IntegrationTestBase {

    @Autowired
    private UserDataAccessObject something;

    @Test
    public void getUsers() throws APIException, Auth0Exception {

        List<User> users = something.getUsers();

        assertThat(users).isNotEmpty();
    }
}
