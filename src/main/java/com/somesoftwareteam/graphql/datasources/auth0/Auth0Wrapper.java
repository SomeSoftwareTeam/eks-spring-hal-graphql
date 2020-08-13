package com.somesoftwareteam.graphql.datasources.auth0;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * https://auth0.com/docs/extensions/authorization-extension/v2/api-access
 * https://auth0.com/docs/api/authorization-extension
 * https://github.com/auth0/auth0-java
 * https://auth0.com/docs/api/management/v2
 */
@Service
public class Auth0Wrapper {

    private final AuthAPI authAPI;
    private final Logger logger;
    private final ManagementAPI managementAPI;
    private ZonedDateTime lastToken;

    // TODO: inject
    private TokenHolder auth0CoreApiTokenHolder;
    private TokenHolder auth0AuthExtTokenHolder;

    public Auth0Wrapper(AuthAPI authAPI) throws Auth0Exception {
        this.authAPI = authAPI;
        this.logger = LoggerFactory.getLogger("Auth0Wrapper");
        this.managementAPI = new ManagementAPI("somesoftwareteam.auth0.com", "");
        requestTokens();
    }

    public List<User> getAuth0CoreUsers() throws Auth0Exception {
        if (tokensAreExpired()) requestTokens();
        logger.info("Requesting authorization core users");
        UserFilter filter = new UserFilter().withPage(0, 20);
        com.auth0.net.Request<UsersPage> request = managementAPI.users().list(filter);
        UsersPage page = request.execute();
        return page.getItems();
    }

    public List<User> searchAuth0CoreUsers(String luceneSearchString) throws Auth0Exception {
        if (tokensAreExpired()) requestTokens();
        logger.info("Searching authorization core users");
        UserFilter filter = new UserFilter().withQuery(luceneSearchString).withPage(0, 20);
        com.auth0.net.Request<UsersPage> request = managementAPI.users().list(filter);
        UsersPage page = request.execute();
        return page.getItems();
    }

    private boolean tokensAreExpired() {
        long d = Duration.between(lastToken, ZonedDateTime.now()).toSeconds();
        return d > auth0CoreApiTokenHolder.getExpiresIn() || d > auth0AuthExtTokenHolder.getExpiresIn();
    }

    private void requestTokens() throws Auth0Exception {
        logger.info("Requesting Auth0 token");
        lastToken = ZonedDateTime.now();
        AuthRequest auth0CoreApiAuthRequest = authAPI.requestToken("https://somesoftwareteam.auth0.com/api/v2/");
        auth0CoreApiTokenHolder = auth0CoreApiAuthRequest.execute();
        managementAPI.setApiToken(auth0CoreApiTokenHolder.getAccessToken());
        AuthRequest auth0AuthExtAuthRequest = authAPI.requestToken("urn:auth0-authz-api");
        auth0AuthExtTokenHolder = auth0AuthExtAuthRequest.execute();
    }
}
