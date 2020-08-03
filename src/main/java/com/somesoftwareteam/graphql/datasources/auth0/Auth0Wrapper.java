package com.somesoftwareteam.graphql.datasources.auth0;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * https://auth0.com/docs/extensions/authorization-extension/v2/api-access
 * https://github.com/auth0/auth0-java
 */
@Service
public class Auth0Wrapper {

    private final AuthAPI authAPI;

    public Auth0Wrapper() {

        authAPI = new AuthAPI(
                "somesoftwareteam.auth0.com",
                System.getenv("AUTH0_CLIENT_ID"),
                System.getenv("AUTH0_CLIENT_SECRET"));
    }

    public List<User> getAuth0Users() throws Auth0Exception {
        AuthRequest authRequest = authAPI.requestToken("https://somesoftwareteam.auth0.com/api/v2/");
        TokenHolder holder = authRequest.execute();
        ManagementAPI managementAPI = new ManagementAPI("somesoftwareteam.auth0.com", holder.getAccessToken());
        UserFilter filter = new UserFilter().withPage(0, 20);
        Request<UsersPage> request = managementAPI.users().list(filter);
        UsersPage page = request.execute();
        return page.getItems();
    }
}
