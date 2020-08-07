package com.somesoftwareteam.graphql;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }

    // https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}