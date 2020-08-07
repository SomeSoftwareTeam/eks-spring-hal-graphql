package com.somesoftwareteam.graphql.datasources.auth0;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.AuthRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * https://auth0.com/docs/extensions/authorization-extension/v2/api-access
 * https://github.com/auth0/auth0-java
 * https://auth0.com/docs/api/authorization-extension
 */
@Service
public class Auth0Wrapper {

    private final AuthAPI authAPI;
    private final Logger logger;
    private final ObjectMapper objectMapper;
    private ZonedDateTime lastToken;


    // TODO: inject
    private TokenHolder auth0CoreApiTokenHolder;
    private TokenHolder auth0AuthExtTokenHolder;

    public Auth0Wrapper(AuthAPI authAPI, ObjectMapper objectMapper) throws Auth0Exception {
        this.authAPI = authAPI;
        this.logger = LoggerFactory.getLogger("Auth0Wrapper");
        this.objectMapper = objectMapper;
        requestTokens();
    }

    public List<User> getAuth0CoreUsers() throws Auth0Exception {
        if (expired()) requestTokens();
        logger.info("Requesting authorization core users");
        ManagementAPI managementAPI = new ManagementAPI("somesoftwareteam.auth0.com", auth0CoreApiTokenHolder.getAccessToken());
        UserFilter filter = new UserFilter().withPage(0, 20);
        com.auth0.net.Request<UsersPage> request = managementAPI.users().list(filter);
        UsersPage page = request.execute();
        return page.getItems();
    }

    public List<Group> getAuth0AuthorizationExtensionGroups() throws IOException {
        if (expired()) requestTokens();
        logger.info("Requesting authorization extension groups");
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .header("Authorization", "Bearer " + auth0AuthExtTokenHolder.getAccessToken())
                .url("https://somesoftwareteam.us12.webtask.io/adf6e2f2b84784b57522e3b19dfc9201/api/groups")
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            JsonNode node = objectMapper.readTree(response.body().string());
            return Arrays.asList(objectMapper.treeToValue(node.get("groups"), Group[].class));
        }
    }

    private boolean expired() {
        long d = Duration.between(lastToken, ZonedDateTime.now()).toSeconds();
        return d > auth0CoreApiTokenHolder.getExpiresIn() || d > auth0AuthExtTokenHolder.getExpiresIn();
    }

    private void requestTokens() throws Auth0Exception {
        logger.info("Requesting Auth0 token");
        lastToken = ZonedDateTime.now();
        AuthRequest auth0CoreApiAuthRequest = authAPI.requestToken("https://somesoftwareteam.auth0.com/api/v2/");
        auth0CoreApiTokenHolder = auth0CoreApiAuthRequest.execute();
        AuthRequest auth0AuthExtAuthRequest = authAPI.requestToken("urn:auth0-authz-api");
        auth0AuthExtTokenHolder = auth0AuthExtAuthRequest.execute();
    }
}
