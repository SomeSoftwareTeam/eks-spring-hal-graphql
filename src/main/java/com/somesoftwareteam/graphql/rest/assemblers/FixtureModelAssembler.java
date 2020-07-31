package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class FixtureModelAssembler implements RepresentationModelAssembler<Fixture, EntityModel<Fixture>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public FixtureModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Fixture> toModel(@NonNull Fixture fixture) {

        List<Link> links = new ArrayList<>();
        links.add(repositoryEntityLinks.linkToItemResource(Fixture.class, fixture.getId()).withSelfRel());
        links.add(repositoryEntityLinks.linkToCollectionResource(Fixture.class).withRel("fixtures"));

        if (!Objects.isNull(fixture.getProperty()))
            links.add(repositoryEntityLinks.linkToItemResource(Property.class, fixture.getProperty().getId()));

        return EntityModel.of(fixture, links);
    }
}