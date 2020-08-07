package com.somesoftwareteam.graphql.datasources.auth0;

import com.auth0.client.auth.AuthAPI;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Auth0Configuration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    AuthAPI authAPI() {
        return new AuthAPI(
                "somesoftwareteam.auth0.com",
                System.getenv("AUTH0_CLIENT_ID"),
                System.getenv("AUTH0_CLIENT_SECRET"));
    }
}
