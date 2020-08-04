package com.somesoftwareteam.graphql.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.FixtureRepository;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.datasources.mysql.specification.SpecificationBuilder;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.somesoftwareteam.graphql.graphql.ResolverUtils.createPageRequest;
import static com.somesoftwareteam.graphql.graphql.ResolverUtils.createSort;

/**
 * https://github.com/marmelab/react-admin/tree/master/packages/ra-data-graphql-simple
 */
@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:fixtures')")
public class FixtureResolver {

    private final EntityCreator entityCreator;
    private final MyAclService myAclService;
    private final FixtureRepository fixtureRepository;
    private final PropertyRepository propertyRepository;
    private final SpecificationBuilder<Fixture> specificationBuilder;

    public FixtureResolver(EntityCreator entityCreator,
                           MyAclService myAclService, FixtureRepository fixtureRepository,
                           PropertyRepository propertyRepository, SpecificationBuilder<Fixture> specificationBuilder) {
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.fixtureRepository = fixtureRepository;
        this.propertyRepository = propertyRepository;
        this.specificationBuilder = specificationBuilder;
    }

    @GraphQLQuery(name = "Fixture", description = "Get fixture by primary id")
    public Fixture fixture(@GraphQLId @GraphQLNonNull Long id) {
        return fixtureRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GraphQLQuery(name = "allFixtures", description = "Get fixture records")
    public List<Fixture> allFixtures(Integer page, Integer perPage, String sortField, String sortOrder,
                                     FixtureFilter filter) {
        Specification<Fixture> spec = specificationBuilder.createSpecFromFilter(filter);
        Sort sort = createSort(sortOrder, sortField);
        PageRequest pageRequest = createPageRequest(page, perPage, sort);
        return fixtureRepository.findAll(spec, pageRequest).getContent();
    }

    @GraphQLQuery(name = "_allFixturesMeta", description = "Get fixture records metadata")
    public ListMetadata allFixturesMeta(Integer page, Integer perPage, FixtureFilter filter) {
        Specification<Fixture> spec = specificationBuilder.createSpecFromFilter(filter);
        PageRequest pageRequest = createPageRequest(page, perPage);
        Long count = fixtureRepository.findAll(spec, pageRequest).getTotalElements();
        return new ListMetadata(count);
    }

//    @PreAuthorize("hasAuthority('SCOPE_write:fixtures')")
//    @GraphQLMutation(name = "createFixture", description = "Create a new fixture record")
//    public Fixture createFixture(@GraphQLId @GraphQLNonNull Long propertyId, @GraphQLNonNull String name,
//                                 JsonNode attributes) {
//        Property property = propertyRepository.findById(propertyId).orElseThrow(ResourceNotFoundException::new);
//        Fixture fixture = new Fixture(name, null, attributes, property);
//        entityCreator.setOwnerAndPersistEntity(fixture);
//        myAclService.createAccessControlList(Fixture.class, fixture.getId());
//        return fixture;
//    }
//
//    @PreAuthorize("hasAuthority('SCOPE_write:fixtures')")
//    @GraphQLMutation(name = "updateFixture", description = "Update a fixture record")
//    public Fixture updateFixture(@GraphQLId @GraphQLNonNull Long id, String name, JsonNode attributes) {
//        Fixture fixture = fixtureRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
//        fixture.setAttributes(attributes);
//        fixture.setName(name);
//        fixtureRepository.save(fixture);
//        return fixture;
//    }
//
//    @PreAuthorize("hasAuthority('SCOPE_write:fixtures')")
//    @GraphQLMutation(name = "deleteFixture", description = "Delete a fixture record")
//    public Fixture deleteFixture(@GraphQLId @GraphQLNonNull Long id) {
//        Fixture fixture = fixtureRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
//        fixtureRepository.deleteById(id);
//        return fixture;
//    }
}


