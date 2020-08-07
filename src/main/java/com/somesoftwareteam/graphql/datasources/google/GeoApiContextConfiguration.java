package com.somesoftwareteam.graphql.datasources.google;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class GeoApiContextConfiguration {

    @Value("${google.api_key}")
    private String key;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder().apiKey(key).build();
    }
}
