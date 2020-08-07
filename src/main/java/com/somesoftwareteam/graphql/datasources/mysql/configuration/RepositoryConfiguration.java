package com.somesoftwareteam.graphql.datasources.mysql.configuration;

import com.somesoftwareteam.graphql.datasources.mysql.entities.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

/**
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#customizing-sdr.configuring-cors
 */
@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config
                .getCorsRegistry()
                .addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://leslie.somesoftwareteam.com")
                .allowedMethods("GET", "PUT", "POST", "DELETE");

        config
                .exposeIdsFor(Club.class)
                .exposeIdsFor(Document.class)
                .exposeIdsFor(Item.class)
                .exposeIdsFor(Fixture.class)
                .exposeIdsFor(Property.class)
                .exposeIdsFor(Verification.class);
    }
}