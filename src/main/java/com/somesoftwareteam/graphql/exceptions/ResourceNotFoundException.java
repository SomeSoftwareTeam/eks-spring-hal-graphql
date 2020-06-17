package com.somesoftwareteam.graphql.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Long id) {
        super("Could not find resource " + id);
    }
}