package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
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
public class PropertyModelAssembler implements RepresentationModelAssembler<Property, EntityModel<Property>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public PropertyModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Property> toModel(@NonNull Property property) {

        List<Link> links = new ArrayList<>();
        links.add(repositoryEntityLinks.linkToItemResource(Property.class, property.getId()).withSelfRel());
        links.add(repositoryEntityLinks.linkToCollectionResource(Property.class).withRel("properties"));
        return EntityModel.of(property, links);
    }
}