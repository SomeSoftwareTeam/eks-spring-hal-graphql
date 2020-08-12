package com.somesoftwareteam.graphql.datasources.mysql.handlers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Organization;
import com.somesoftwareteam.graphql.datasources.mysql.entities.OrganizationMember;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.OrganizationMemberRepository;
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
public class OrganizationRepositoryEventHandlers {

    private final AuthenticationFacade authenticationFacade;
    private final OrganizationMemberRepository organizationMemberRepository;

    public OrganizationRepositoryEventHandlers(AuthenticationFacade authenticationFacade,
                                               OrganizationMemberRepository organizationMemberRepository) {
        this.authenticationFacade = authenticationFacade;
        this.organizationMemberRepository = organizationMemberRepository;
    }

    @HandleAfterCreate
    public void handleAfterCreate(Organization organization) throws IOException {
        addPrincipalAsOrganizationMember(organization);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Organization organization) {
        System.out.println("here");
    }

    private void addPrincipalAsOrganizationMember(Organization organization) {
        String memberId = authenticationFacade.getCurrentPrincipalName();
        organizationMemberRepository.save(new OrganizationMember(organization.getId(), memberId));
    }
}