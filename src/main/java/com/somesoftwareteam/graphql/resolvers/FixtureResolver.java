package com.somesoftwareteam.graphql.resolvers;

import com.somesoftwareteam.graphql.acl.MyAclService;
import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.repositories.FixtureRepository;
import com.somesoftwareteam.graphql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * https://github.com/marmelab/react-admin/tree/master/packages/ra-data-graphql-simple
 */
@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:fixtures')")
public class FixtureResolver {

    private final AuthenticationFacade authenticationFacade;
    private final EntityCreator entityCreator;
    private final MyAclService myAclService;
    private final FixtureRepository fixtureRepository;
    private final PropertyRepository propertyRepository;

    public FixtureResolver(AuthenticationFacade authenticationFacade, EntityCreator entityCreator,
                           MyAclService myAclService, FixtureRepository fixtureRepository,
                           PropertyRepository propertyRepository) {
        this.authenticationFacade = authenticationFacade;
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.fixtureRepository = fixtureRepository;
        this.propertyRepository = propertyRepository;
    }

    @GraphQLQuery(name = "Fixture", description = "Get fixture by primary id")
    public Fixture fixture(@GraphQLId @GraphQLNonNull Long id) {
        return fixtureRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GraphQLQuery(name = "allFixtures", description = "Get fixture records")
    public List<Fixture> allFixtures(@GraphQLNonNull Integer page, @GraphQLNonNull Integer perPage,
                                     @GraphQLNonNull String sortField, @GraphQLNonNull String sortOrder,
                                     @GraphQLNonNull FixtureFilter filter) {
        // TODO: implement filter
        Sort sort = sortOrder.equals("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return fixtureRepository.findAll(PageRequest.of(page, perPage, sort)).getContent();
    }

    @GraphQLQuery(name = "_allFixturesMeta", description = "Get fixture records metadata")
    public ListMetadata allFixturesMeta(@GraphQLNonNull Integer page, @GraphQLNonNull Integer perPage,
                                        @GraphQLNonNull FixtureFilter filter) {
        Long count = fixtureRepository.findAll(PageRequest.of(page, perPage)).getTotalElements();
        return new ListMetadata(count);
    }

    @GraphQLMutation(name = "createFixture", description = "Create a new fixture record")
    public Fixture createFixture(@GraphQLId @GraphQLNonNull Long propertyId, @GraphQLNonNull String name) {
        String owner = authenticationFacade.getCurrentPrincipalName();
        Property property = propertyRepository.findById(propertyId).orElseThrow(ResourceNotFoundException::new);
        Fixture fixture = new Fixture(name, owner, JacksonUtil.toJsonNode("{}"), property);
        entityCreator.persistEntity(fixture);
        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(fixture);
        return fixture;
    }

    @GraphQLMutation(name = "updateFixture", description = "Update a fixture record")
    public Fixture updateFixture(@GraphQLId @GraphQLNonNull Long id, String name) {
        Fixture fixture = fixtureRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        fixture.setName(name);
        fixtureRepository.save(fixture);
        return fixture;
    }

    @GraphQLMutation(name = "deleteFixture", description = "Delete a fixture record")
    public Fixture deleteFixture(@GraphQLId @GraphQLNonNull Long id) {
        Fixture fixture = fixtureRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        fixtureRepository.deleteById(id);
        return fixture;
    }
}


