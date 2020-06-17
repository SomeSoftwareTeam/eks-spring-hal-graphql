package com.somesoftwareteam.graphql;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.somesoftwareteam.graphql.entities.Fixture;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import static com.somesoftwareteam.graphql.TestTokenProvider.getToken;

/**
 * Help from:
 * https://www.baeldung.com/spring-boot-testing
 * https://www.baeldung.com/spring-jpa-test-in-memory-database
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 */
class GraphqlTests extends IntegrationTestBase {

    @Test
    @Transactional
    @WithMockUser(username = "google|12345")
    public void graphqlQueryRuns() {

        // https://www.apollographql.com/docs/android/essentials/get-started/

        Fixture fixture = createTestFixtureWithAccessControlListForUser("google|12345");

        ApolloClient client = buildApolloClient();

        GetFixtureQuery query = GetFixtureQuery.builder().id(fixture.getId()).build();

        ApolloCall.Callback<GetFixtureQuery.Data> callback = buildCallback();

        client.query(query).enqueue(callback);
    }

    private ApolloClient buildApolloClient() {

        String token = getToken();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                    builder.header("Authorization", "Bearer " + token);
                    return chain.proceed(builder.build());
                }).build();

        return ApolloClient.builder().okHttpClient(httpClient)
                .serverUrl("http://localhost:" + port + "/graphql").build();
    }

    private ApolloCall.Callback<GetFixtureQuery.Data> buildCallback() {

        return new ApolloCall.Callback<>() {

            @Override
            public void onResponse(@NotNull Response<GetFixtureQuery.Data> dataResponse) {

                GetFixtureQuery.Data data = dataResponse.getData();

                if (data == null) return;

                System.out.println(data.getFixture());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                System.out.println(e.getMessage());
            }
        };
    }
}
