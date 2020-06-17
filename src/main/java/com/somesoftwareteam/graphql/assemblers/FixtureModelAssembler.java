package com.somesoftwareteam.graphql.assemblers;

import com.somesoftwareteam.graphql.entities.Fixture;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class FixtureModelAssembler implements RepresentationModelAssembler<Fixture, EntityModel<Fixture>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public FixtureModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NotNull
    @Override
    public EntityModel<Fixture> toModel(@NotNull Fixture fixture) {
        return EntityModel.of(fixture,
                repositoryEntityLinks.linkToItemResource(Fixture.class, fixture.getId()).withSelfRel(),
                repositoryEntityLinks.linkToCollectionResource(Fixture.class).withRel("fixtures"));
    }
}