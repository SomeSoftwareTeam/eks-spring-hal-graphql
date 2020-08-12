package com.somesoftwareteam.graphql.datasources.mysql.handlers;

import com.somesoftwareteam.graphql.datasources.auth0.Auth0Wrapper;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Club;
import com.somesoftwareteam.graphql.datasources.mysql.entities.ClubMember;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.ClubMemberRepository;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
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
    private final ClubMemberRepository clubMemberRepository;

    public ClubRepositoryEventHandlers(AuthenticationFacade authenticationFacade, Auth0Wrapper auth0Wrapper,
                                       ClubMemberRepository clubMemberRepository) {
        this.authenticationFacade = authenticationFacade;
        this.clubMemberRepository = clubMemberRepository;
    }

    @HandleAfterCreate
    public void handleAfterCreate(Club club) throws IOException {
        addPrincipalAsClubMember(club);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Club club) {
        System.out.println("here");
    }

    private void addPrincipalAsClubMember(Club club) {
        String memberId = authenticationFacade.getCurrentPrincipalName();
        clubMemberRepository.save(new ClubMember(club.getId(), memberId));
    }
}