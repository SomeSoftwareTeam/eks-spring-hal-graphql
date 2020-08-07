package com.somesoftwareteam.graphql.datasources.mysql.configuration;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeometryFactoryConfiguration {

    @Bean
    GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 3857);
    }
}
