package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class FixtureModelAssembler implements RepresentationModelAssembler<Fixture, EntityModel<Fixture>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public FixtureModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Fixture> toModel(@NonNull Fixture fixture) {
        return EntityModel.of(fixture,
                repositoryEntityLinks.linkToItemResource(Fixture.class, fixture.getId()).withSelfRel(),
                repositoryEntityLinks.linkToCollectionResource(Fixture.class).withRel("fixtures"));
    }
}