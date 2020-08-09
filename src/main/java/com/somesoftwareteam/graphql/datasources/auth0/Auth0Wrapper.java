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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;
    private ZonedDateTime lastToken;
    private final String extBaseUrl = "https://somesoftwareteam.us12.webtask.io/adf6e2f2b84784b57522e3b19dfc9201/api";

    // TODO: inject
    private TokenHolder auth0CoreApiTokenHolder;
    private TokenHolder auth0AuthExtTokenHolder;

    public Auth0Wrapper(AuthAPI authAPI, ObjectMapper objectMapper) throws Auth0Exception {
        this.authAPI = authAPI;
        this.logger = LoggerFactory.getLogger("Auth0Wrapper");
        this.managementAPI = new ManagementAPI("somesoftwareteam.auth0.com", "");
        this.objectMapper = objectMapper;
        this.okHttpClient = new OkHttpClient();
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






    public List<Auth0ExtGroup> getAuth0ExtGroups() throws IOException {
        logger.info("Requesting authorization extension groups");
        okhttp3.Request request = getRequestBuilder(extBaseUrl + "/groups").build();
        try (okhttp3.Response response = okHttpClient.newCall(request).execute()) {
            String body = Objects.isNull(response.body()) ? "" : response.body().string();
            if (!response.isSuccessful())
                throw new Auth0Exception("Failed to get authorization groups: " + response.code() + " " + body);
            JsonNode node = objectMapper.readTree(body);
            return Arrays.asList(objectMapper.treeToValue(node.get("groups"), Auth0ExtGroup[].class));
        }
    }

    public Auth0ExtGroup createAuth0ExtGroup(String name, String description) throws IOException {
        logger.info("Creating authorization extension group");
        if (Objects.isNull(name) || Objects.isNull(description))
            throw new IllegalArgumentException("Auth0 ext group must have non-null name and description");
        Map<String, String> map = Map.of("name", name, "description", description);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(map));
        okhttp3.Request request = getRequestBuilder(extBaseUrl + "/groups").post(requestBody).build();
        try (okhttp3.Response response = okHttpClient.newCall(request).execute()) {
            String body = Objects.isNull(response.body()) ? "" : response.body().string();
            if (!response.isSuccessful())
                throw new Auth0Exception("Failed to create authorization group: " + response.code() + " " + body);
            JsonNode node = objectMapper.readTree(body);
            return objectMapper.treeToValue(node, Auth0ExtGroup.class);
        }
    }

    public void updateAuth0ExtGroup(Auth0ExtGroup group) throws IOException {
        logger.info("Creating authorization extension group");
        if (Objects.isNull(group.getName()) || Objects.isNull(group.getDescription()))
            throw new IllegalArgumentException("Auth0 ext group must have non-null name and description");
        Map<String, String> map = Map.of("name", group.getName(), "description", group.getDescription());
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(map));
        okhttp3.Request request = getRequestBuilder(extBaseUrl + "/groups/" + group.getId()).put(requestBody).build();
        try (okhttp3.Response response = okHttpClient.newCall(request).execute()) {
            String body = Objects.isNull(response.body()) ? "" : response.body().string();
            if (!response.isSuccessful())
                throw new Auth0Exception("Failed to create authorization group: " + response.code() + " " + body);
        }
    }

    public void addUserToAuth0ExtGroup(Auth0ExtGroup group, String userId) throws IOException {
        logger.info("Adding user to authorization extension group");
        String[] data = {userId};
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(data));
        okhttp3.Request request =
                getRequestBuilder(extBaseUrl + "/groups/" + group.getId() + "/members").patch(requestBody).build();
        try (okhttp3.Response response = okHttpClient.newCall(request).execute()) {
            String body = Objects.isNull(response.body()) ? "" : response.body().string();
            if (!response.isSuccessful())
                throw new Auth0Exception("Failed to add user to authorization group: " + response.code() + " " + body);
        }
    }

    public void deleteAuthorizationGroup(Auth0ExtGroup group) throws IOException {
        logger.info("Deleting authorization extension group");
        okhttp3.Request request = getRequestBuilder(extBaseUrl + "/groups/" + group.getId()).delete().build();
        try (okhttp3.Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) return;
            String body = Objects.isNull(response.body()) ? "" : response.body().string();
            throw new Auth0Exception("Failed to delete authorization group: " + response.code() + " " + body);
        }
    }

    private okhttp3.Request.Builder getRequestBuilder(String url) throws Auth0Exception {
        if (tokensAreExpired()) requestTokens();
        return new okhttp3.Request.Builder()
                .header("Authorization", "Bearer " + auth0AuthExtTokenHolder.getAccessToken())
                .url(url);
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
