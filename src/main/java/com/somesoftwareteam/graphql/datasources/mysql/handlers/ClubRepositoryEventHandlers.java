package com.somesoftwareteam.graphql.datasources.mysql.handlers;

import com.somesoftwareteam.graphql.datasources.auth0.Auth0ExtGroup;
import com.somesoftwareteam.graphql.datasources.auth0.Auth0Wrapper;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Club;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#events
 */
@Component
@RepositoryEventHandler
public class ClubRepositoryEventHandlers {

    private final AuthenticationFacade authenticationFacade;
    private final Auth0Wrapper auth0Wrapper;

    public ClubRepositoryEventHandlers(AuthenticationFacade authenticationFacade, Auth0Wrapper auth0Wrapper) {
        this.authenticationFacade = authenticationFacade;
        this.auth0Wrapper = auth0Wrapper;
    }

    @HandleAfterCreate
    public void handleAfterCreate(Club club) throws IOException {
        Auth0ExtGroup newGroup = auth0Wrapper.createAuth0ExtGroup(club.getId().toString(), club.getDescription());
        String principal = authenticationFacade.getCurrentPrincipalName();
        auth0Wrapper.addUserToAuth0ExtGroup(newGroup, principal);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Club club) {
        System.out.println("here");
    }
}